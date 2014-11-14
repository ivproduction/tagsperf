package aki.launchable

import aki.utils.SQLConnector
import groovy.sql.Sql

//settings

def topAccounts = [
//        281:504992,
        112:1494,
        121:572,
        132:375,
//        1:284,
//        297:274,
//        149:232,
//        128:208,
//        166:190,
//        238:158
]

def maxTagsForLink = 4
def partOfAllLInksTosetTags = 0.66666666

def tagsSize = 997

def accountLinksBatchSize = 100
def completitionsStepSize = 0.005


// launch

def Sql masterDs = new SQLConnector().createMaster();
def Sql masterDsReplica = new SQLConnector().createReplica();

try {

    def deleted = masterDs.executeUpdate("DELETE from LINK_TAGS")
    println "$deleted link-tags deleted"
//    int tagsSize = masterDs.firstRow("SELECT count(*) as count FROM tags").count;
    println "Total tags $tagsSize"

    def initedLinksWithTagsCount = 0;
    def linkedTagsCount = 0;

    println "Top Accounts Size is ${topAccounts.size()} ($topAccounts)"

    topAccounts.each { accountId, linksCount ->
        println "Start Filling tags for Account($accountId) ..."
        println "Account has $linksCount links"
        println "Tags will be linked for ${partOfAllLInksTosetTags * 100}% of all account tags";


        def nextCompleteStep = completitionsStepSize

        for (int from = 0; from < linksCount; from = from + accountLinksBatchSize) {

            def partOfBatchSize = (int) (accountLinksBatchSize * partOfAllLInksTosetTags);

            def sguids = masterDsReplica.rows(
                    "select SGUID from SHORTENED_LINKS s join CAMPAIGNS c on s.CAMPAIGN_ID = c.CAMPAIGN_ID " +
                    "where c.ACCOUNT_ID = $accountId " +
                    "order by SGUID limit $from, $partOfBatchSize"
            )*.SGUID

            sguids.each { linkGuid ->

                def tagIds = RandArray.getRandomIntArray(tagsSize, maxTagsForLink).join(',');

                def randomTagIds = masterDsReplica.rows(
                        "SELECT TAG_ID from tags WHERE TAG_ID IN (" + tagIds + ")"
                )*.TAG_ID

                masterDs.withBatch {stmt->
                    randomTagIds.each { tagId ->
                        stmt.addBatch("INSERT INTO LINK_TAGS VALUES ($accountId, $tagId, '$linkGuid')")
                        linkedTagsCount++;
                    }
                }

                initedLinksWithTagsCount++;
            }

            def completeProgress = (double) from / (double) linksCount
            if (completeProgress >= nextCompleteStep) {
                println "Complete " + (int)(completeProgress * 100) + "%"
                nextCompleteStep = nextCompleteStep + completitionsStepSize
            }
        }

    }

    println 'Total Tags inserted ' + linkedTagsCount
    println 'Total Links linked with tag' + initedLinksWithTagsCount
} finally {
    masterDs.close();
    masterDsReplica.close();
}

class RandArray {
    static Random random = new Random(System.currentTimeMillis());
    static def getRandomIntArray(int maxValue, int maxArrayCount) {
        def randomIntArray = []
        int randomArraySize = (Math.abs(random.nextInt()) % (maxArrayCount)) + 1
        while (((int) randomIntArray.size) < randomArraySize) {
            def nextInt = (Math.abs(random.nextInt()) % (maxValue)) + 1;
            if (!(nextInt in randomIntArray)) {
                randomIntArray << nextInt
            }
        }
        return randomIntArray
    }
}
