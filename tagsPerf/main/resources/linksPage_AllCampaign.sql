-- rendering link page

-- N Clicks Total Panel
SELECT SUM(AGGR.CLICKS) AS CLICKS
FROM ACCOUNT AS AGGR
WHERE
  AGGR.ACCOUNT_ID = 2
  AND AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11';

-- Graf of clicks
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

-- Links list - total count
SELECT COUNT(0) AS COUNT
FROM (
       SELECT U.UGUID
       FROM UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID
                                                        AND
                                                        AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'
       WHERE
         U.ACCOUNT_ID = 2
         AND U.CAMPAIGN_ARCHIVED = 0
         AND U.LINK_ARCHIVED = 0
         AND (U.ORIGINAL_URL LIKE '%%')
       GROUP BY U.UGUID
     ) AS T;


-- Links list - per page
SELECT
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
WHERE
  U.ACCOUNT_ID = 2
  AND U.CAMPAIGN_ARCHIVED = 0
  AND U.LINK_ARCHIVED = 0
  AND (U.ORIGINAL_URL LIKE '%%')
GROUP BY U.UGUID
ORDER BY CREATION_DATE DESC, ORDER_INSERTED DESC
LIMIT 0, 20;

-- links list next page (total count)
SELECT
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
WHERE
  U.ACCOUNT_ID = 2
  AND U.CAMPAIGN_ARCHIVED = 0
  AND U.LINK_ARCHIVED = 0
  AND (U.ORIGINAL_URL LIKE '%%')
GROUP BY U.UGUID
ORDER BY CREATION_DATE DESC, ORDER_INSERTED DESC
LIMIT 20, 20;

