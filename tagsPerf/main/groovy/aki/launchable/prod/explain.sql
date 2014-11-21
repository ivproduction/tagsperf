# total clicks
EXPLAIN SELECT SUM(AGGR.CLICKS) AS CLICKS
        FROM
          UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U
            ON AGGR.UGUID = U.UGUID AND AGGR.DATE BETWEEN '2011-10-13' AND '2014-11-11'
          JOIN post_master.LINK_TAGS AS lt ON lt.SGUID = U.UGUID
          JOIN post_master.TAGS AS t ON t.TAG_ID = lt.TAG_ID
        WHERE U.ACCOUNT_ID = 79764 AND U.CAMPAIGN_ARCHIVED = 0 AND U.LINK_ARCHIVED = 0 AND t.TAG_NAME = 'monetary';

# total clicks - with id

EXPLAIN SELECT SUM(AGGR.CLICKS) AS CLICKS
FROM
  UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID AND AGGR.DATE BETWEEN '2011-10-13' AND '2014-11-11'
  JOIN post_master.LINK_TAGS AS lt ON lt.SGUID = U.UGUID
  JOIN post_master.TAGS AS t ON t.TAG_ID = lt.TAG_ID
WHERE lt.ACCOUNT_ID = 79764 AND U.ACCOUNT_ID = 79764 AND U.CAMPAIGN_ARCHIVED = 0 AND U.LINK_ARCHIVED = 0 AND
      t.TAG_NAME = 'monetary';

# graf
EXPLAIN SELECT
  AGGR.DATE        AS DATE,
  SUM(AGGR.CLICKS) AS CLICKS
FROM
  UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID AND AGGR.DATE BETWEEN '2011-10-13' AND '2014-11-11'
  JOIN post_master.LINK_TAGS AS lt ON lt.SGUID = U.UGUID
  JOIN post_master.TAGS AS t ON t.TAG_ID = lt.TAG_ID
WHERE U.ACCOUNT_ID = 79764 AND U.CAMPAIGN_ARCHIVED = 0 AND U.LINK_ARCHIVED = 0 AND t.TAG_NAME = 'monetary'
GROUP BY AGGR.DATE
HAVING CLICKS != 0
ORDER BY DATE ASC;

# graf with id
EXPLAIN SELECT
  AGGR.DATE        AS DATE,
  SUM(AGGR.CLICKS) AS CLICKS
FROM
  UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID AND AGGR.DATE BETWEEN '2011-10-13' AND '2014-11-11'
  JOIN post_master.LINK_TAGS AS lt ON lt.SGUID = U.UGUID
  JOIN post_master.TAGS AS t ON t.TAG_ID = lt.TAG_ID
WHERE lt.ACCOUNT_ID = 79764 AND U.ACCOUNT_ID = 79764 AND U.CAMPAIGN_ARCHIVED = 0 AND U.LINK_ARCHIVED = 0 AND
      t.TAG_NAME = 'monetary'
GROUP BY AGGR.DATE
HAVING CLICKS != 0
ORDER BY DATE ASC;

# links total old
EXPLAIN SELECT COUNT(0) AS COUNT
FROM (SELECT U.UGUID
      FROM UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U
          ON AGGR.UGUID = U.UGUID AND AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'
      WHERE U.ACCOUNT_ID = 79764 AND U.CAMPAIGN_ARCHIVED = 0 AND U.LINK_ARCHIVED = 0 AND (U.ORIGINAL_URL LIKE '%%')
      GROUP BY U.UGUID) AS T;

# links total new
EXPLAIN SELECT COUNT(0) AS COUNT
FROM (SELECT U.UGUID
      FROM UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U
          ON AGGR.UGUID = U.UGUID AND AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'
        JOIN post_master.LINK_TAGS AS lt ON lt.SGUID = U.UGUID
        JOIN post_master.TAGS AS t ON t.TAG_ID = lt.TAG_ID
      WHERE U.ACCOUNT_ID = 79764 AND U.CAMPAIGN_ARCHIVED = 0 AND U.LINK_ARCHIVED = 0 AND (U.ORIGINAL_URL LIKE '%%') AND
            t.TAG_NAME = 'monetary'
      GROUP BY U.UGUID) AS T;
# links total new with id
EXPLAIN SELECT COUNT(0) AS COUNT
FROM (SELECT U.UGUID
      FROM UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U
          ON AGGR.UGUID = U.UGUID AND AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'
        JOIN post_master.LINK_TAGS AS lt ON lt.SGUID = U.UGUID
        JOIN post_master.TAGS AS t ON t.TAG_ID = lt.TAG_ID
      WHERE lt.ACCOUNT_ID = 79764 AND U.ACCOUNT_ID = 79764 AND U.CAMPAIGN_ARCHIVED = 0 AND U.LINK_ARCHIVED = 0 AND
            (U.ORIGINAL_URL LIKE '%%') AND
            t.TAG_NAME = 'monetary'
      GROUP BY U.UGUID) AS T;

# links list old
EXPLAIN SELECT
  U.CREATION_DATE  AS CREATION_DATE,
  U.CUSTOM_DOMAIN  AS CUSTOM_DOMAIN,
  U.CAMPAIGN_NAME  AS CAMPAIGN_NAME,
  U.HASH           AS HASH,
  U.TYPE           AS TYPE,
  U.ORIGINAL_URL   AS ORIGINAL_URL,
  U.CAMPAIGN_ID    AS CAMPAIGN_ID,
  U.GEO_LINKS      AS GEO_LINKS,
  U.UGUID          AS UGUID,
  U.ALIAS          AS ALIAS,
  SUM(AGGR.CLICKS) AS CLICKS
FROM
  UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID AND AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'
WHERE U.ACCOUNT_ID = 79764 AND U.CAMPAIGN_ARCHIVED = 0 AND U.LINK_ARCHIVED = 0 AND (U.ORIGINAL_URL LIKE '%%')
GROUP BY U.UGUID
ORDER BY CLICKS DESC, ORDER_INSERTED DESC
LIMIT 20, 20;

# links list new
EXPLAIN SELECT
  U.CREATION_DATE  AS CREATION_DATE,
  U.CUSTOM_DOMAIN  AS CUSTOM_DOMAIN,
  U.CAMPAIGN_NAME  AS CAMPAIGN_NAME,
  U.HASH           AS HASH,
  U.TYPE           AS TYPE,
  U.ORIGINAL_URL   AS ORIGINAL_URL,
  U.CAMPAIGN_ID    AS CAMPAIGN_ID,
  U.GEO_LINKS      AS GEO_LINKS,
  U.UGUID          AS UGUID,
  U.ALIAS          AS ALIAS,
  SUM(AGGR.CLICKS) AS CLICKS
FROM
  UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID AND AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'
  JOIN post_master.LINK_TAGS AS lt ON lt.SGUID = U.UGUID
  JOIN post_master.TAGS AS t ON t.TAG_ID = lt.TAG_ID
WHERE U.ACCOUNT_ID = 79764 AND U.CAMPAIGN_ARCHIVED = 0 AND U.LINK_ARCHIVED = 0 AND (U.ORIGINAL_URL LIKE '%%') AND
      t.TAG_NAME = 'monetary'
GROUP BY U.UGUID
ORDER BY CLICKS DESC, ORDER_INSERTED DESC
LIMIT 20, 20;

# links list new with id
EXPLAIN SELECT
  U.CREATION_DATE  AS CREATION_DATE,
  U.CUSTOM_DOMAIN  AS CUSTOM_DOMAIN,
  U.CAMPAIGN_NAME  AS CAMPAIGN_NAME,
  U.HASH           AS HASH,
  U.TYPE           AS TYPE,
  U.ORIGINAL_URL   AS ORIGINAL_URL,
  U.CAMPAIGN_ID    AS CAMPAIGN_ID,
  U.GEO_LINKS      AS GEO_LINKS,
  U.UGUID          AS UGUID,
  U.ALIAS          AS ALIAS,
  SUM(AGGR.CLICKS) AS CLICKS
FROM
  UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID AND AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'
  JOIN post_master.LINK_TAGS AS lt ON lt.SGUID = U.UGUID
  JOIN post_master.TAGS AS t ON t.TAG_ID = lt.TAG_ID
WHERE U.ACCOUNT_ID = 79764 AND lt.ACCOUNT_ID = 79764 AND U.CAMPAIGN_ARCHIVED = 0 AND U.LINK_ARCHIVED = 0 AND
      (U.ORIGINAL_URL LIKE '%%') AND t.TAG_NAME = 'monetary'
GROUP BY U.UGUID
ORDER BY CLICKS DESC, ORDER_INSERTED DESC
LIMIT 20, 20;


#account with few links
# total clicks
EXPLAIN SELECT SUM(AGGR.CLICKS) AS CLICKS
        FROM
          UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U
            ON AGGR.UGUID = U.UGUID AND AGGR.DATE BETWEEN '2011-10-13' AND '2014-11-11'
          JOIN post_master.LINK_TAGS AS lt ON lt.SGUID = U.UGUID
          JOIN post_master.TAGS AS t ON t.TAG_ID = lt.TAG_ID
        WHERE U.ACCOUNT_ID= 281 AND U.CAMPAIGN_ARCHIVED = 0 AND U.LINK_ARCHIVED = 0 AND t.TAG_NAME = 'monetary';

# total clicks - with id

EXPLAIN SELECT SUM(AGGR.CLICKS) AS CLICKS
        FROM
          UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID AND AGGR.DATE BETWEEN '2011-10-13' AND '2014-11-11'
          JOIN post_master.LINK_TAGS AS lt ON lt.SGUID = U.UGUID
          JOIN post_master.TAGS AS t ON t.TAG_ID = lt.TAG_ID
        WHERE lt.ACCOUNT_ID= 281 AND U.ACCOUNT_ID= 281 AND U.CAMPAIGN_ARCHIVED = 0 AND U.LINK_ARCHIVED = 0 AND
              t.TAG_NAME = 'monetary';

# graf
EXPLAIN SELECT
          AGGR.DATE        AS DATE,
          SUM(AGGR.CLICKS) AS CLICKS
        FROM
          UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID AND AGGR.DATE BETWEEN '2011-10-13' AND '2014-11-11'
          JOIN post_master.LINK_TAGS AS lt ON lt.SGUID = U.UGUID
          JOIN post_master.TAGS AS t ON t.TAG_ID = lt.TAG_ID
        WHERE U.ACCOUNT_ID= 281 AND U.CAMPAIGN_ARCHIVED = 0 AND U.LINK_ARCHIVED = 0 AND t.TAG_NAME = 'monetary'
        GROUP BY AGGR.DATE
        HAVING CLICKS != 0
        ORDER BY DATE ASC;

# graf with id
EXPLAIN SELECT
          AGGR.DATE        AS DATE,
          SUM(AGGR.CLICKS) AS CLICKS
        FROM
          UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID AND AGGR.DATE BETWEEN '2011-10-13' AND '2014-11-11'
          JOIN post_master.LINK_TAGS AS lt ON lt.SGUID = U.UGUID
          JOIN post_master.TAGS AS t ON t.TAG_ID = lt.TAG_ID
        WHERE lt.ACCOUNT_ID= 281 AND U.ACCOUNT_ID= 281 AND U.CAMPAIGN_ARCHIVED = 0 AND U.LINK_ARCHIVED = 0 AND
              t.TAG_NAME = 'monetary'
        GROUP BY AGGR.DATE
        HAVING CLICKS != 0
        ORDER BY DATE ASC;

# links total old
EXPLAIN SELECT COUNT(0) AS COUNT
        FROM (SELECT U.UGUID
              FROM UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U
                  ON AGGR.UGUID = U.UGUID AND AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'
              WHERE U.ACCOUNT_ID= 281 AND U.CAMPAIGN_ARCHIVED = 0 AND U.LINK_ARCHIVED = 0 AND (U.ORIGINAL_URL LIKE '%%')
              GROUP BY U.UGUID) AS T;

# links total new
EXPLAIN SELECT COUNT(0) AS COUNT
FROM (SELECT U.UGUID
      FROM UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U
          ON AGGR.UGUID = U.UGUID AND AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'
        JOIN post_master.LINK_TAGS AS lt ON lt.SGUID = U.UGUID
        JOIN post_master.TAGS AS t ON t.TAG_ID = lt.TAG_ID
      WHERE U.ACCOUNT_ID= 281 AND U.CAMPAIGN_ARCHIVED = 0 AND U.LINK_ARCHIVED = 0 AND (U.ORIGINAL_URL LIKE '%%') AND
            t.TAG_NAME = 'monetary'
      GROUP BY U.UGUID) AS T;
# links total new with id
EXPLAIN SELECT COUNT(0) AS COUNT
        FROM (SELECT U.UGUID
              FROM UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U
                  ON AGGR.UGUID = U.UGUID AND AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'
                JOIN post_master.LINK_TAGS AS lt ON lt.SGUID = U.UGUID
                JOIN post_master.TAGS AS t ON t.TAG_ID = lt.TAG_ID
              WHERE lt.ACCOUNT_ID= 281 AND U.ACCOUNT_ID= 281 AND U.CAMPAIGN_ARCHIVED = 0 AND U.LINK_ARCHIVED = 0 AND
                    (U.ORIGINAL_URL LIKE '%%') AND
                    t.TAG_NAME = 'monetary'
              GROUP BY U.UGUID) AS T;

# links list old
EXPLAIN SELECT
          U.CREATION_DATE  AS CREATION_DATE,
          U.CUSTOM_DOMAIN  AS CUSTOM_DOMAIN,
          U.CAMPAIGN_NAME  AS CAMPAIGN_NAME,
          U.HASH           AS HASH,
          U.TYPE           AS TYPE,
          U.ORIGINAL_URL   AS ORIGINAL_URL,
          U.CAMPAIGN_ID    AS CAMPAIGN_ID,
          U.GEO_LINKS      AS GEO_LINKS,
          U.UGUID          AS UGUID,
          U.ALIAS          AS ALIAS,
          SUM(AGGR.CLICKS) AS CLICKS
        FROM
          UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID AND AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'
        WHERE U.ACCOUNT_ID= 281 AND U.CAMPAIGN_ARCHIVED = 0 AND U.LINK_ARCHIVED = 0 AND (U.ORIGINAL_URL LIKE '%%')
        GROUP BY U.UGUID
        ORDER BY CLICKS DESC, ORDER_INSERTED DESC
        LIMIT 20, 20;

# links list new
EXPLAIN SELECT
          U.CREATION_DATE  AS CREATION_DATE,
          U.CUSTOM_DOMAIN  AS CUSTOM_DOMAIN,
          U.CAMPAIGN_NAME  AS CAMPAIGN_NAME,
          U.HASH           AS HASH,
          U.TYPE           AS TYPE,
          U.ORIGINAL_URL   AS ORIGINAL_URL,
          U.CAMPAIGN_ID    AS CAMPAIGN_ID,
          U.GEO_LINKS      AS GEO_LINKS,
          U.UGUID          AS UGUID,
          U.ALIAS          AS ALIAS,
          SUM(AGGR.CLICKS) AS CLICKS
        FROM
          UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID AND AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'
          JOIN post_master.LINK_TAGS AS lt ON lt.SGUID = U.UGUID
          JOIN post_master.TAGS AS t ON t.TAG_ID = lt.TAG_ID
        WHERE U.ACCOUNT_ID= 281 AND U.CAMPAIGN_ARCHIVED = 0 AND U.LINK_ARCHIVED = 0 AND (U.ORIGINAL_URL LIKE '%%') AND
              t.TAG_NAME = 'monetary'
        GROUP BY U.UGUID
        ORDER BY CLICKS DESC, ORDER_INSERTED DESC
        LIMIT 20, 20;

# links list new with id
EXPLAIN SELECT
          U.CREATION_DATE  AS CREATION_DATE,
          U.CUSTOM_DOMAIN  AS CUSTOM_DOMAIN,
          U.CAMPAIGN_NAME  AS CAMPAIGN_NAME,
          U.HASH           AS HASH,
          U.TYPE           AS TYPE,
          U.ORIGINAL_URL   AS ORIGINAL_URL,
          U.CAMPAIGN_ID    AS CAMPAIGN_ID,
          U.GEO_LINKS      AS GEO_LINKS,
          U.UGUID          AS UGUID,
          U.ALIAS          AS ALIAS,
          SUM(AGGR.CLICKS) AS CLICKS
        FROM
          UGUID AS AGGR RIGHT JOIN VIEW_UGUID AS U ON AGGR.UGUID = U.UGUID AND AGGR.DATE BETWEEN '2014-10-13' AND '2014-11-11'
          JOIN post_master.LINK_TAGS AS lt ON lt.SGUID = U.UGUID
          JOIN post_master.TAGS AS t ON t.TAG_ID = lt.TAG_ID
        WHERE U.ACCOUNT_ID= 281 AND lt.ACCOUNT_ID= 281 AND U.CAMPAIGN_ARCHIVED = 0 AND U.LINK_ARCHIVED = 0 AND
              (U.ORIGINAL_URL LIKE '%%') AND t.TAG_NAME = 'monetary'
        GROUP BY U.UGUID
        ORDER BY CLICKS DESC, ORDER_INSERTED DESC
        LIMIT 20, 20;