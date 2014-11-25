package aki.launchable.prod

/**
 * @author Alex Kirilov  12-11-14
 */
class PerfQueries2 {

//    static def accountId = 1;
//    public static def String devEv = '';
    public static def String devEv = '_dev';

    static def linksTable79764 = "SELECT\n" +
            "          U.CREATION_DATE  AS CREATION_DATE,\n" +
            "          U.CUSTOM_DOMAIN  AS CUSTOM_DOMAIN,\n" +
            "          U.CAMPAIGN_NAME  AS CAMPAIGN_NAME,\n" +
            "          U.HASH           AS HASH,\n" +
            "          U.TYPE           AS TYPE,\n" +
            "          U.ORIGINAL_URL   AS ORIGINAL_URL,\n" +
            "          U.CAMPAIGN_ID    AS CAMPAIGN_ID,\n" +
            "          U.GEO_LINKS      AS GEO_LINKS,\n" +
            "          U.UGUID          AS UGUID,\n" +
            "          U.ALIAS          AS ALIAS,\n" +
            "          SUM(AGGR.CLICKS) AS CLICKS\n" +
            "        FROM\n" +
            "          UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID AND AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'\n" +
            "          JOIN post_master_dev.LINK_TAGS AS lt ON lt.SGUID = U.UGUID\n" +
            "          JOIN post_master_dev.TAGS AS t ON t.TAG_ID = lt.TAG_ID\n" +
            "        WHERE lt.ACCOUNT_ID=accountId AND U.CAMPAIGN_ARCHIVED = 0 AND U.LINK_ARCHIVED = 0 AND\n" +
            "              (U.ORIGINAL_URL LIKE '%%') AND t.TAG_NAME = 'qtagName'\n" +
            "        GROUP BY U.UGUID\n" +
            "        ORDER BY CLICKS DESC, ORDER_INSERTED DESC\n" +
            "        LIMIT 20, 20;"


    static def linksTable281 = "SELECT\n" +
            "          U.CREATION_DATE  AS CREATION_DATE,\n" +
            "          U.CUSTOM_DOMAIN  AS CUSTOM_DOMAIN,\n" +
            "          U.CAMPAIGN_NAME  AS CAMPAIGN_NAME,\n" +
            "          U.HASH           AS HASH,\n" +
            "          U.TYPE           AS TYPE,\n" +
            "          U.ORIGINAL_URL   AS ORIGINAL_URL,\n" +
            "          U.CAMPAIGN_ID    AS CAMPAIGN_ID,\n" +
            "          U.GEO_LINKS      AS GEO_LINKS,\n" +
            "          U.UGUID          AS UGUID,\n" +
            "          U.ALIAS          AS ALIAS,\n" +
            "          SUM(AGGR.CLICKS) AS CLICKS\n" +
            "        FROM\n" +
            "          UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID AND AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'\n" +
            "          JOIN post_master_dev.LINK_TAGS AS lt ON lt.SGUID = U.UGUID\n" +
            "          JOIN post_master_dev.TAGS AS t ON t.TAG_ID = lt.TAG_ID\n" +
            "        WHERE lt.ACCOUNT_ID=accountId AND U.CAMPAIGN_ARCHIVED = 0 AND U.LINK_ARCHIVED = 0 AND\n" +
            "              (U.ORIGINAL_URL LIKE '%%') AND t.TAG_NAME = 'qtagName'\n" +
            "        GROUP BY U.UGUID\n" +
            "        ORDER BY CLICKS DESC, ORDER_INSERTED DESC\n" +
            "        LIMIT 20, 20;"


    static def queries = [linksTable79764, linksTable281]

    static def queriesNames = ['linksTable79764',
                               'linksTable281'
    ]

}