package aki.launchable.prod

/**
 * @author Alex Kirilov  12-11-14
 */
class PerfQueries3 {

//    static def accountId = 1;
//    public static def String devEv = '_dev';
    public static def String devEv = '';

    static def totalClicks = "SELECT SUM(AGGR.CLICKS) AS CLICKS\n" +
            "FROM ACCOUNT AS AGGR\n" +
            "WHERE\n" +
            "  AGGR.ACCOUNT_ID = accountId\n" +
            "  AND AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11';"


    static def totalClicks_new_noTag = "SELECT SUM(AGGR.CLICKS) AS CLICKS\n" +
            "FROM\n" +
            "  UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID\n" +
            "                                              AND\n" +
            "                                              AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'\n" +
            "WHERE\n" +
            "  U.ACCOUNT_ID = accountId\n" +
            "  AND U.CAMPAIGN_ARCHIVED = 0\n" +
            "  AND U.LINK_ARCHIVED = 0;"

    static def totalClicks_new_withTag = "SELECT SUM(AGGR.CLICKS) AS CLICKS\n" +
            "FROM\n" +
            "  UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID\n" +
            "                                              AND\n" +
            "                                              AGGR.DATE BETWEEN '2011-10-13' AND '2014-11-11'\n" +
            "  JOIN post_master${devEv}.LINK_TAGS AS lt ON lt.SGUID = U.UGUID\n" +
            "  JOIN post_master${devEv}.TAGS AS t ON t.TAG_ID = lt.TAG_ID\n" +
            "WHERE\n" +
            "  U.ACCOUNT_ID = accountId\n" +
            "  AND U.CAMPAIGN_ARCHIVED = 0\n" +
            "  AND U.LINK_ARCHIVED = 0\n" +
            "  AND t.TAG_NAME = 'qtagName';"

    static def totalClicks_new_withTag_accountId = "SELECT SUM(AGGR.CLICKS) AS CLICKS\n" +
            "FROM\n" +
            "  UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID\n" +
            "                                              AND\n" +
            "                                              AGGR.DATE BETWEEN '2011-10-13' AND '2014-11-11'\n" +
            "  JOIN post_master${devEv}.LINK_TAGS AS lt ON lt.SGUID = U.UGUID\n" +
            "  JOIN post_master${devEv}.TAGS AS t ON t.TAG_ID = lt.TAG_ID\n" +
            "WHERE\n" +
            "  lt.ACCOUNT_ID = accountId\n" +
            "  AND U.CAMPAIGN_ARCHIVED = 0\n" +
            "  AND U.LINK_ARCHIVED = 0\n" +
            "  AND t.TAG_NAME = 'qtagName'"


    static def grafOld = "SELECT\n" +
            "  AGGR.DATE        AS DATE,\n" +
            "  SUM(AGGR.CLICKS) AS CLICKS\n" +
            "FROM ACCOUNT AS AGGR\n" +
            "WHERE\n" +
            "  AGGR.ACCOUNT_ID = accountId\n" +
            "  AND AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'\n" +
            "GROUP BY AGGR.DATE\n" +
            "HAVING\n" +
            "  CLICKS != 0\n" +
            "ORDER BY DATE ASC;"

    static def grafNew = "SELECT\n" +
            "          AGGR.DATE        AS DATE,\n" +
            "          SUM(AGGR.CLICKS) AS CLICKS\n" +
            "        FROM UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID\n" +
            "                                                         AND\n" +
            "                                                         AGGR.DATE BETWEEN '2011-10-13' AND '2014-11-11'\n" +
            "          JOIN post_master${devEv}.LINK_TAGS AS lt ON lt.SGUID = U.UGUID\n" +
            "          JOIN post_master${devEv}.TAGS AS t ON t.TAG_ID = lt.TAG_ID\n" +
            "        WHERE\n" +
            "          U.ACCOUNT_ID = accountId\n" +
            "          AND U.CAMPAIGN_ARCHIVED = 0\n" +
            "          AND U.LINK_ARCHIVED = 0\n" +
            "          AND t.TAG_NAME = 'qtagName'\n" +
            "        GROUP BY AGGR.DATE\n" +
            "        HAVING\n" +
            "          CLICKS != 0\n" +
            "        ORDER BY DATE ASC;"

    static def grafNewWithId = "SELECT\n" +
            "          AGGR.DATE        AS DATE,\n" +
            "          SUM(AGGR.CLICKS) AS CLICKS\n" +
            "        FROM UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID\n" +
            "                                                         AND\n" +
            "                                                         AGGR.DATE BETWEEN '2011-10-13' AND '2014-11-11'\n" +
            "          JOIN post_master${devEv}.LINK_TAGS AS lt ON lt.SGUID = U.UGUID\n" +
            "          JOIN post_master${devEv}.TAGS AS t ON t.TAG_ID = lt.TAG_ID\n" +
            "        WHERE\n" +
            "          lt.ACCOUNT_ID = accountId\n" +
            "          AND U.CAMPAIGN_ARCHIVED = 0\n" +
            "          AND U.LINK_ARCHIVED = 0\n" +
            "          AND t.TAG_NAME = 'qtagName'\n" +
            "        GROUP BY AGGR.DATE\n" +
            "        HAVING\n" +
            "          CLICKS != 0\n" +
            "        ORDER BY DATE ASC;"

    static def linkListCountOld = "SELECT COUNT(0) AS COUNT\n" +
            "FROM (\n" +
            "       SELECT U.UGUID\n" +
            "       FROM UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID\n" +
            "                                                        AND\n" +
            "                                                        AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'\n" +
            "       WHERE\n" +
            "         U.ACCOUNT_ID = accountId\n" +
            "         AND U.CAMPAIGN_ARCHIVED = 0\n" +
            "         AND U.LINK_ARCHIVED = 0\n" +
            "         AND (U.ORIGINAL_URL LIKE '%%')\n" +
            "       GROUP BY U.UGUID\n" +
            "     ) AS T;"

    static def linkListCount = "SELECT COUNT(0) AS COUNT\n" +
            "FROM (\n" +
            "       SELECT U.UGUID\n" +
            "       FROM UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID\n" +
            "                                                        AND\n" +
            "                                                        AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'\n" +
            "         JOIN post_master${devEv}.LINK_TAGS AS lt ON lt.SGUID = U.UGUID\n" +
            "         JOIN post_master${devEv}.TAGS AS t ON t.TAG_ID = lt.TAG_ID\n" +
            "       WHERE\n" +
            "         U.ACCOUNT_ID = accountId\n" +
            "         AND U.CAMPAIGN_ARCHIVED = 0\n" +
            "         AND U.LINK_ARCHIVED = 0\n" +
            "         AND (U.ORIGINAL_URL LIKE '%%')\n" +
            "         AND t.TAG_NAME = 'qtagName'\n" +
            "       GROUP BY U.UGUID\n" +
            "     ) AS T;"

    static def linkListCountWithId = "SELECT COUNT(0) AS COUNT\n" +
            "FROM (\n" +
            "       SELECT U.UGUID\n" +
            "       FROM UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID\n" +
            "                                                        AND\n" +
            "                                                        AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'\n" +
            "         JOIN post_master${devEv}.LINK_TAGS AS lt ON lt.SGUID = U.UGUID\n" +
            "         JOIN post_master${devEv}.TAGS AS t ON t.TAG_ID = lt.TAG_ID\n" +
            "       WHERE\n" +
            "         lt.ACCOUNT_ID = accountId\n" +
            "         AND U.CAMPAIGN_ARCHIVED = 0\n" +
            "         AND U.LINK_ARCHIVED = 0\n" +
            "         AND (U.ORIGINAL_URL LIKE '%%')\n" +
            "         AND t.TAG_NAME = 'qtagName'\n" +
            "       GROUP BY U.UGUID\n" +
            "     ) AS T;"

    static def linkListOld = "SELECT\n" +
            "  U.CREATION_DATE AS CREATION_DATE,\n" +
            "  U.CUSTOM_DOMAIN AS CUSTOM_DOMAIN,\n" +
            "  U.CAMPAIGN_NAME AS CAMPAIGN_NAME,\n" +
            "  U.HASH AS HASH,\n" +
            "  U.TYPE AS TYPE,\n" +
            "  U.ORIGINAL_URL AS ORIGINAL_URL,\n" +
            "  U.CAMPAIGN_ID AS CAMPAIGN_ID,\n" +
            "  U.GEO_LINKS AS GEO_LINKS,\n" +
            "  U.UGUID AS UGUID,\n" +
            "  U.ALIAS AS ALIAS,\n" +
            "  SUM(AGGR.CLICKS) AS CLICKS\n" +
            "FROM UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID\n" +
            "                                                 AND\n" +
            "                                                 AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'\n" +
            "WHERE\n" +
            "  U.ACCOUNT_ID = accountId\n" +
            "  AND U.CAMPAIGN_ARCHIVED = 0\n" +
            "  AND U.LINK_ARCHIVED = 0\n" +
            "  AND (U.ORIGINAL_URL LIKE '%%')\n" +
            "GROUP BY U.UGUID\n" +
            "ORDER BY CLICKS DESC, ORDER_INSERTED DESC\n" +
            "LIMIT 20,20;"

    static def linkListNew = "SELECT\n" +
            "  U.CREATION_DATE AS CREATION_DATE,\n" +
            "  U.CUSTOM_DOMAIN AS CUSTOM_DOMAIN,\n" +
            "  U.CAMPAIGN_NAME AS CAMPAIGN_NAME,\n" +
            "  U.HASH AS HASH,\n" +
            "  U.TYPE AS TYPE,\n" +
            "  U.ORIGINAL_URL AS ORIGINAL_URL,\n" +
            "  U.CAMPAIGN_ID AS CAMPAIGN_ID,\n" +
            "  U.GEO_LINKS AS GEO_LINKS,\n" +
            "  U.UGUID AS UGUID,\n" +
            "  U.ALIAS AS ALIAS,\n" +
            "  SUM(AGGR.CLICKS) AS CLICKS\n" +
            "FROM UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID\n" +
            "                                                 AND\n" +
            "                                                 AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'\n" +
            "  JOIN post_master${devEv}.LINK_TAGS AS lt ON lt.SGUID = U.UGUID\n" +
            "  JOIN post_master${devEv}.TAGS AS t ON t.TAG_ID = lt.TAG_ID\n" +
            "WHERE\n" +
            "  U.ACCOUNT_ID = accountId\n" +
            "  AND U.CAMPAIGN_ARCHIVED = 0\n" +
            "  AND U.LINK_ARCHIVED = 0\n" +
            "  AND (U.ORIGINAL_URL LIKE '%%')\n" +
            "  AND t.TAG_NAME = 'qtagName'\n" +
            "GROUP BY U.UGUID\n" +
            "ORDER BY CLICKS DESC, ORDER_INSERTED DESC\n" +
            "LIMIT 20,20"

    static def linkListNewId = "SELECT\n" +
            "  U.CREATION_DATE AS CREATION_DATE,\n" +
            "  U.CUSTOM_DOMAIN AS CUSTOM_DOMAIN,\n" +
            "  U.CAMPAIGN_NAME AS CAMPAIGN_NAME,\n" +
            "  U.HASH AS HASH,\n" +
            "  U.TYPE AS TYPE,\n" +
            "  U.ORIGINAL_URL AS ORIGINAL_URL,\n" +
            "  U.CAMPAIGN_ID AS CAMPAIGN_ID,\n" +
            "  U.GEO_LINKS AS GEO_LINKS,\n" +
            "  U.UGUID AS UGUID,\n" +
            "  U.ALIAS AS ALIAS,\n" +
            "  SUM(AGGR.CLICKS) AS CLICKS\n" +
            "FROM UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID\n" +
            "                                                 AND\n" +
            "                                                 AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'\n" +
            "  JOIN post_master${devEv}.LINK_TAGS AS lt ON lt.SGUID = U.UGUID\n" +
            "  JOIN post_master${devEv}.TAGS AS t ON t.TAG_ID = lt.TAG_ID\n" +
            "WHERE\n" +
            "  U.ACCOUNT_ID = accountId\n" +
            "  AND lt.ACCOUNT_ID = accountId\n" +
            "  AND U.CAMPAIGN_ARCHIVED = 0\n" +
            "  AND U.LINK_ARCHIVED = 0\n" +
            "  AND (U.ORIGINAL_URL LIKE '%%')\n" +
            "  AND t.TAG_NAME = 'qtagName'\n" +
            "GROUP BY U.UGUID\n" +
            "ORDER BY CLICKS DESC, ORDER_INSERTED DESC\n" +
            "LIMIT 20,20";

    static def queries = [totalClicks,
                          totalClicks_new_noTag, totalClicks_new_withTag, totalClicks_new_withTag_accountId, grafOld, grafNew, grafNewWithId,
                          linkListCountOld, linkListCount, linkListCountWithId, linkListOld, linkListNew, linkListNewId
    ]
    static def queriesNames = ['totalClicks',
                               'totalClicks_new_noTag', 'totalClicks_new_withTag', 'totalClicks_new_withTag_accountId', 'grafOld', 'grafNew', 'grafNewWithId',
                               'linkListCountOld', 'linkListCount', 'linkListCountWithId', 'linkListOld', 'linkListNew', 'linkListNewId'
    ]

}