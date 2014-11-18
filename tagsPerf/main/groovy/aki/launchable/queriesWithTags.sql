-- N Clicks Total Panel

/*   old

SELECT SUM(AGGR.CLICKS) AS CLICKS
FROM ACCOUNT AS AGGR
WHERE
  AGGR.ACCOUNT_ID = 2
  AND AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11';


  */

SELECT SUM(AGGR.CLICKS) AS CLICKS
FROM
  UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID
                                              AND
                                              AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'
WHERE
  U.ACCOUNT_ID = 2
  AND U.CAMPAIGN_ARCHIVED = 0
  AND U.LINK_ARCHIVED = 0;

-- with tag

SELECT SUM(AGGR.CLICKS) AS CLICKS
FROM
  UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID
                                              AND
                                              AGGR.DATE BETWEEN '2011-10-13' AND '2014-11-11'
  JOIN post_master_dev.LINK_TAGS AS lt ON lt.SGUID = AGGR.UGUID
  JOIN post_master_dev.TAGS AS t ON t.TAG_ID = lt.TAG_ID
WHERE
  U.ACCOUNT_ID = 1
  AND U.CAMPAIGN_ARCHIVED = 0
  AND U.LINK_ARCHIVED = 0
  AND t.TAG_NAME = 'naval';

-- add account_id filtering

SELECT SUM(AGGR.CLICKS) AS CLICKS
FROM
  UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID
                                              AND
                                              AGGR.DATE BETWEEN '2011-10-13' AND '2014-11-11'
  JOIN post_master_dev.LINK_TAGS AS lt ON lt.SGUID = AGGR.UGUID
  JOIN post_master_dev.TAGS AS t ON t.TAG_ID = lt.TAG_ID
WHERE
  lt.ACCOUNT_ID = 1
  AND U.ACCOUNT_ID = 1
  AND U.CAMPAIGN_ARCHIVED = 0
  AND U.LINK_ARCHIVED = 0
  AND t.TAG_NAME = 'naval';


-- Graf of clicks
/* old

SELECT
  AGGR.DATE        AS DATE,
  SUM(AGGR.CLICKS) AS CLICKS
FROM ACCOUNT AS AGGR
WHERE
  AGGR.ACCOUNT_ID = 2
  AND AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'
GROUP BY AGGR.DATE
HAVING
  CLICKS != 0
ORDER BY DATE ASC;
*/

SELECT
          AGGR.DATE        AS DATE,
          SUM(AGGR.CLICKS) AS CLICKS
        FROM UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID
                                                         AND
                                                         AGGR.DATE BETWEEN '2011-10-13' AND '2014-11-11'
          JOIN post_master_dev.LINK_TAGS AS lt ON lt.SGUID = AGGR.UGUID
          JOIN post_master_dev.TAGS AS t ON t.TAG_ID = lt.TAG_ID
        WHERE
          U.ACCOUNT_ID = 1
          AND U.CAMPAIGN_ARCHIVED = 0
          AND U.LINK_ARCHIVED = 0
          AND t.TAG_NAME = 'naval'
        GROUP BY AGGR.DATE
        HAVING
          CLICKS != 0
        ORDER BY DATE ASC;

SELECT
          AGGR.DATE        AS DATE,
          SUM(AGGR.CLICKS) AS CLICKS
        FROM UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID
                                                         AND
                                                         AGGR.DATE BETWEEN '2011-10-13' AND '2014-11-11'
          JOIN post_master_dev.LINK_TAGS AS lt ON lt.SGUID = AGGR.UGUID
          JOIN post_master_dev.TAGS AS t ON t.TAG_ID = lt.TAG_ID
        WHERE
          lt.ACCOUNT_ID = 1
          AND U.ACCOUNT_ID = 1
          AND U.CAMPAIGN_ARCHIVED = 0
          AND U.LINK_ARCHIVED = 0
          AND t.TAG_NAME = 'naval'
        GROUP BY AGGR.DATE
        HAVING
          CLICKS != 0
        ORDER BY DATE ASC;

-- Links list - total count

SELECT COUNT(0) AS COUNT
FROM (
       SELECT U.UGUID
       FROM UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID
                                                        AND
                                                        AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'
         JOIN post_master_dev.LINK_TAGS AS lt ON lt.SGUID = AGGR.UGUID
         JOIN post_master_dev.TAGS AS t ON t.TAG_ID = lt.TAG_ID
       WHERE
         U.ACCOUNT_ID = 2
         AND U.CAMPAIGN_ARCHIVED = 0
         AND U.LINK_ARCHIVED = 0
         AND (U.ORIGINAL_URL LIKE '%%')
         AND t.TAG_NAME = 'nacelle'
       GROUP BY U.UGUID
     ) AS T;

-- links list next page (total count)
EXPLAIN SELECT
          U.TYPE           AS TYPE,
          U.GEO_LINKS      AS GEO_LINKS,
          U.CAMPAIGN_NAME  AS CAMPAIGN_NAME,
          U.UGUID          AS UGUID,
          U.HASH           AS HASH,
          U.ALIAS          AS ALIAS,
          U.ORIGINAL_URL   AS ORIGINAL_URL,
          U.CUSTOM_DOMAIN  AS CUSTOM_DOMAIN,
          U.CAMPAIGN_ID    AS CAMPAIGN_ID,
          U.CREATION_DATE  AS CREATION_DATE,
          SUM(AGGR.CLICKS) AS CLICKS
        FROM UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID
                                                         AND
                                                         AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'
          JOIN post_master_dev.LINK_TAGS AS lt ON lt.SGUID = AGGR.UGUID
          JOIN post_master_dev.TAGS AS t ON t.TAG_ID = lt.TAG_ID
        WHERE
          lt.ACCOUNT_ID = 2
          AND U.ACCOUNT_ID = 2
          AND U.CAMPAIGN_ARCHIVED = 0
          AND U.LINK_ARCHIVED = 0
          AND (U.ORIGINAL_URL LIKE '%%')
          AND t.TAG_NAME = 'nacelle'
        GROUP BY U.UGUID
        ORDER BY CREATION_DATE DESC, ORDER_INSERTED DESC
        LIMIT 0, 20;

