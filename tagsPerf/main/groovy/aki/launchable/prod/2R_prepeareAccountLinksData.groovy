package aki.launchable.prod

import aki.utils.ProdSQLConnector
import groovy.sql.Sql

def Sql masterRepDs = new ProdSQLConnector().createReplica();

try {
    def topAccountLimit = 20;
    def simpleAccountLimit = 100;
    def simpleAccountMaxLinks = 3000;

    def file = new File("accoutnLinksData.txt")

    file.withWriter { out ->
        masterRepDs.eachRow("SELECT c.ACCOUNT_ID as ACCOUNT_ID, count(l.SGUID) as linksCount " +
                "from shortened_links as l JOIN campaigns as c ON c.CAMPAIGN_ID=l.CAMPAIGN_ID " +
                "GROUP BY c.ACCOUNT_ID HAVING linksCount < $simpleAccountMaxLinks ORDER BY linksCount DESC LIMIT $simpleAccountLimit") {
            row ->
                out.writeLine("${row.ACCOUNT_ID},${row.linksCount}")
        };

        masterRepDs.eachRow("SELECT c.ACCOUNT_ID as ACCOUNT_ID, count(l.SGUID) as linksCount " +
                "from shortened_links as l JOIN campaigns as c ON c.CAMPAIGN_ID=l.CAMPAIGN_ID GROUP BY c.ACCOUNT_ID ORDER BY linksCount DESC LIMIT $topAccountLimit") {
            row ->
                out.writeLine("${row.ACCOUNT_ID},${row.linksCount}")
        };
    }

} catch (e) {

} finally {
    masterRepDs.close();
}