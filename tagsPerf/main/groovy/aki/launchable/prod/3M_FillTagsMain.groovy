package aki.launchable.prod

import aki.utils.ProdSQLConnector
import groovy.sql.Sql

import java.util.concurrent.TimeUnit

//settings

def maxTagsForLink = 4
def partOfAllLInksTosetTags = 0.66666666


def accountLinksBatchSize = 900
def completitionsStepSize = 0.005

// read working accounts from file

def workingAccountLinksMap = new LinkedHashMap();
new File('accoutnLinksData.txt').eachLine { line ->
    if (!line.startsWith('#')) {
        def k = line.split(',')[0] as int;
        def v = (int) line.split(',')[1] as int;
        workingAccountLinksMap[k] = v;
    }
}

// launch

def Sql masterDs = new ProdSQLConnector().createMaster();
def Sql masterDsReplica = new ProdSQLConnector().createReplica();

try {

    def allTagIds = (masterDsReplica.rows("SELECT TAG_ID from tags WHERE TAG_ID ORDER by TAG_ID"))*.TAG_ID
    tagsSize = allTagIds.size();

    println "Total tags $tagsSize"

    def initedLinksWithTagsCount = 0;
    def linkedTagsCount = 0;
    def long processedAllLinksCount = 0;

    println "Working Accounts Size is ${workingAccountLinksMap.size()} ($workingAccountLinksMap)"
    def long totalLinksCount = 0;
    workingAccountLinksMap.each { k, v ->
        totalLinksCount += v;
    }
    println "Total links is ${totalLinksCount}"

    def startTime = System.currentTimeMillis();
    workingAccountLinksMap.each { accountId, accountLinksCount ->
        println "Start Filling tags for Account($accountId) ..."
        println "Account has $accountLinksCount links"
        println "Tags will be linked for ${partOfAllLInksTosetTags * 100}% of all account tags";


        def nextCompleteStep = completitionsStepSize
        def long accLinksProcessed = 0;


        for (int from = 0; from < accountLinksCount; from = from + accountLinksBatchSize) {
            // 1 - get next random links guids ids
            def partOfBatchSize = (int) (accountLinksBatchSize * partOfAllLInksTosetTags);
            def sguids = masterDsReplica.rows(
                    "select SGUID from SHORTENED_LINKS s join CAMPAIGNS c on s.CAMPAIGN_ID = c.CAMPAIGN_ID " +
                            "where c.ACCOUNT_ID = $accountId " +
                            "order by SGUID limit $from, $partOfBatchSize"
            )*.SGUID

            // 2 - prepare and execute batch insert of random tags into all fetched guids
            masterDs.withBatch { stmt ->
                sguids.each { linkGuid ->
                    def randomTagsIds = RandArray.selectRandomElements(allTagIds, maxTagsForLink)
                    randomTagsIds.each { tagId ->
                        stmt.addBatch("INSERT INTO LINK_TAGS VALUES ($accountId, $tagId, '$linkGuid')")
                        linkedTagsCount++;
                    }
                    initedLinksWithTagsCount++;
                }

            }

            //3 - save progress data and print progress and stats
            accLinksProcessed += sguids.size();
            processedAllLinksCount += accountLinksBatchSize;
            def accountAllLinkProcessed = from + accountLinksBatchSize
            def completeProgress = (double) accountAllLinkProcessed / (double) accountLinksCount
            if (completeProgress >= nextCompleteStep) {
                println "Account Links Progress: Complete (${accLinksProcessed} - ${accountAllLinkProcessed}) ${(int) (completeProgress * 100)}% ${getTimePastInSec(startTime)} " +
                        "sec. Est: ${processedAllLinksCount} of ${totalLinksCount} -> ${getEstimatedTime(startTime, totalLinksCount, processedAllLinksCount)} to complete. Speed ${getSpeed(startTime, processedAllLinksCount)}/sec"
                nextCompleteStep = nextCompleteStep + completitionsStepSize
            }
        }

    }

    //print total stats
    println 'Total Tags inserted ' + linkedTagsCount
    println 'Total Links linked with tag ' + initedLinksWithTagsCount
    println 'Time is ' + (System.currentTimeMillis() - startTime) / 1000 + ' sec'
    println "AVG Speed ${getSpeed(startTime, processedAllLinksCount)}/sec"
} finally {
    masterDs.close();
    masterDsReplica.close();
}

class RandArray {
    static Random random = new Random(System.currentTimeMillis());

    static def selectRandomElements(List list, int maxArrayCount) {
        def randomIntArray = []
        int randomArraySize = (Math.abs(random.nextInt()) % (maxArrayCount)) + 1
        def result = []
        while (((int) randomIntArray.size) < randomArraySize) {
            def nextInt = (Math.abs(random.nextInt()) % (list.size()));
            if (!(nextInt in randomIntArray)) {
                randomIntArray << nextInt
                result << list[nextInt]
            }
        }
        result;
    }
}

def getTimePastInSec(long startTime) {
    ((System.currentTimeMillis() - startTime) / 1000);
}

def getEstimatedTime(long startTime, long totalLinksCount, long linksProcessed) {
    def milisecPerLink = ((System.currentTimeMillis() - startTime)) / linksProcessed;
    def result = ((totalLinksCount - linksProcessed) * milisecPerLink) as long
    return String.format("%d hours, %d min, %d sec",
            TimeUnit.MILLISECONDS.toHours(result),
            TimeUnit.MILLISECONDS.toMinutes(result) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(result)),
            TimeUnit.MILLISECONDS.toSeconds(result) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(result))
    );
}

def getSpeed(long startTime, long linksProcessed) {
    (linksProcessed / (((System.currentTimeMillis() - startTime)) / 1000)) as long;
}