package aki.tags

import groovy.sql.Sql

/**
 * @author Alex Kirilov  27-10-14
 */
class LinkTagsManager {
    private static LinkTagsManager instance;
    final private Sql sql;
    final Random random = new Random();
    public static ArrayList preDefinedTags = "tag1 tag2 tag2tag1 text ggg nokia sony android nexus".split()
    def linkIds

    LinkTagsManager(sql) {
        this.sql = sql;
        ArrayList.metaClass.getRand = { number ->
            if (number == 0) {
                return delegate[new Random().nextInt(delegate.size)]
            } else {
                def tempList = []
                def counter = 0
                while (counter > number) {
                    tempList.add(delegate[Math.abs(new Random().nextInt(delegate.size))])
                    counter++
                }
                return tempList
            }
        }

        linkIds = this.sql.rows("select SHORTENED_LINK_ID as id from SHORTENED_LINKS");

    }

    static def createRandomTags(int count) {
        getInstance().insertLinks(count);
    }

    static LinkTagsManager getInstance() {
        if (this.instance == null) {
            this.instance = new LinkTagsManager(new SQLConnector().create())
        }
        return this.instance
    }

    def deleteAllLinks() {
        sql.execute("delete from temp_tags_whith_accountid");
    }

    def insertLinks(int max) {
        (1..max).each { postFix ->
            try {
                def linksId = linkIds.getRand(0).get('id');
                def linkTag = preDefinedTags.getRand(0);
//                def accountId = sql.rows("select ACCOUNT_ID from CAMPAIGNS where CAMPAIGN_ID=(select CAMPAIGN_ID from SHORTENED_LINKS where SHORTENED_LINK_ID=${linksId})")[0].get('ACCOUNT_ID')
//                sql.execute("insert into temp_tags_whith_accountid values(${accountId}, ${linkTag}, ${linksId});")
                sql.execute("insert into temp_tags values(${linkTag}, ${linksId});")


//                linkIds.each { item ->
//                    def linksId1 = item.get('id');
//                    def tagValue = preDefinedTags.getRand(0) + ' ' + preDefinedTags.getRand(0) + ' ' + preDefinedTags.getRand(0)
//                    sql.execute("update SHORTENED_LINKS set TAGS= ${tagValue} WHERE SHORTENED_LINK_ID=${linksId1}")
//                }
            } catch (e) {
            }
        }
    }

    def getLinksCount() {
        sql.firstRow("select count(*) as count from temp_tags_whith_accountid").getProperty("count");
    }
}
