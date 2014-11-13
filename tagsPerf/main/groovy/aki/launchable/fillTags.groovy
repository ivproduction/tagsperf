package aki.launchable

import aki.utils.SQLConnector
import groovy.sql.Sql

//settings

def fillTagsDataAndSchema = false;
def calculateTopAccounts = true;
def accountIds = []
def maxTagsForLink = 4
def accountLinksBatchSize = 100
def partOfAllLInksTosetTags = 0.66666666
def topAccountLimit = 10;
def completitionsStepSize = 0.005

// launch

try {

    def Sql masterDs = new SQLConnector().createMaster();
    def Sql statsDs = new SQLConnector().createStats();

//add tables
    if (fillTagsDataAndSchema) {
        def text = new File("tagsSchema.sql").text
        masterDs.execute(text)

        //filTagsTablewithTags
        TagsDics.taglist_ultra_short.each { tag ->
            masterDs.execute("INSERT IGNORE INTO TAGS (TAG_NAME) VALUES ($tag)");
        }
    }

    masterDs.execute("DELETE from LINK_TAGS")

    if (calculateTopAccounts) {
        accountIds = []
        masterDs.eachRow("SELECT c.ACCOUNT_ID, count(l.SGUID) from shortened_links as l JOIN campaigns as c ON c.CAMPAIGN_ID=l.CAMPAIGN_ID GROUP BY c.ACCOUNT_ID ORDER BY count(*) DESC LIMIT $topAccountLimit") {
            row -> accountIds << row.ACCOUNT_ID
        };
    }

    int tagsSize = masterDs.firstRow("SELECT count(*) as count FROM tags").count;

    println "Total tags $tagsSize"

    def initedLinksWithTagsCount = 0;
    def linkedTagsCount = 0;

    def accountIdsSize = accountIds.size()
    println "Top Accounts Size is $accountIdsSize ($accountIds)"

    accountIds.each { accountId ->
        println "Start Filling tags for Account($accountId) ..."

        def linksCount = masterDs.firstRow("SELECT count(*) as count from shortened_links " +
                "where CAMPAIGN_ID IN (SELECT CAMPAIGN_ID FROM campaigns WHERE ACCOUNT_ID = $accountId) ").count


        println "Account has $linksCount links"
        def partOfAllLInksTosetTagsPersent = (int) partOfAllLInksTosetTags * 100
        println "Tags will be linked for $partOfAllLInksTosetTagsPersent% of all account tags";



        def nextCompleteStep = completitionsStepSize

        for (int i = 0; i < linksCount; i = i + accountLinksBatchSize) {
            def from = i;
            def sguids = [];
            def partOfBatchSize = (int) (accountLinksBatchSize * partOfAllLInksTosetTags);
            masterDs.eachRow("SELECT SGUID from shortened_links where CAMPAIGN_ID IN (SELECT CAMPAIGN_ID FROM " +
                    "campaigns WHERE ACCOUNT_ID = $accountId) ORDER BY SGUID LIMIT $from, $partOfBatchSize") {
                row -> sguids << row.SGUID;
            };

            sguids.each { linkGuid ->
                def tagIds = getRandomIntArray(tagsSize, maxTagsForLink).join(',');
                def randomTagIds = []
                masterDs.eachRow("SELECT TAG_ID from tags WHERE TAG_ID IN (" + tagIds + ")") {
                    row -> randomTagIds << row.TAG_ID
                };
//                println randomTagIds;
                randomTagIds.each { tagId ->
//                println "INSERT INTO LINK_TAGS VALUES ($accountId, $tagId, $linkGuid )";
                    masterDs.execute("INSERT INTO LINK_TAGS VALUES ($accountId, $tagId, $linkGuid )");
                    linkedTagsCount++;
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
    statsDs.close();
}

def getRandomIntArray(int maxValue, int maxArrayCount) {
    def randomIntArray = []
    Random random = new Random(System.currentTimeMillis());
    int randomArraySize = (Math.abs(random.nextInt()) % (maxArrayCount)) + 1
    while (((int) randomIntArray.size) < randomArraySize) {
        def nextInt = (Math.abs(random.nextInt()) % (maxValue)) + 1;
        if (!(nextInt in randomIntArray)) {
            randomIntArray << nextInt
        }
    }
    return randomIntArray
}