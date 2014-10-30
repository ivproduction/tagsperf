#
# STRUCTURE
#

# --temp_tags--
CREATE TABLE temp_tags (
  TAG_NAME          VARCHAR(255) NOT NULL,
  SHORTENED_LINK_ID BIGINT(20)   NOT NULL,
  PRIMARY KEY (TAG_NAME, SHORTENED_LINK_ID),
  CONSTRAINT FK_temp_tags_TO_SHORTENED_LINKS FOREIGN KEY (SHORTENED_LINK_ID) REFERENCES SHORTENED_LINKS (SHORTENED_LINK_ID)
)
  ENGINE =InnoDB;

# --temp_tags_whith_accountid--

CREATE TABLE temp_tags_whith_accountid (
  ACCOUNT_ID        INT(11)      NOT NULL,
  TAG_NAME          VARCHAR(255) NOT NULL,
  SHORTENED_LINK_ID BIGINT(20)   NOT NULL,
  PRIMARY KEY (ACCOUNT_ID, TAG_NAME, SHORTENED_LINK_ID),
  CONSTRAINT FK_temp_tags_whith_accountid_TO_ACCOUNTS FOREIGN KEY (ACCOUNT_ID) REFERENCES ACCOUNTS (ACCOUNT_ID),
  CONSTRAINT FK_temp_tags_whith_accountid_TO_SHORTENED_LINKS FOREIGN KEY (SHORTENED_LINK_ID) REFERENCES SHORTENED_LINKS (SHORTENED_LINK_ID)
)
  ENGINE =InnoDB;


#  tags in shortened_links single field
ALTER TABLE SHORTENED_LINKS ADD COLUMN TAGS TEXT;


#  tags ids
CREATE TABLE temp_tagsids (
  TAG_ID   BIGINT(20)   NOT NULL,
  TAG_NAME VARCHAR(255) NOT NULL,
  PRIMARY KEY (TAG_ID),
  UNIQUE KEY temp_tagsids_tag_name (TAG_NAME)
)
  ENGINE =InnoDB;

CREATE TABLE temp_tagsids_to_links (
  SHORTENED_LINK_ID BIGINT(20) NOT NULL,
  TAG_ID            BIGINT(20) NOT NULL,
  PRIMARY KEY (SHORTENED_LINK_ID, TAG_ID),
  CONSTRAINT FK_temp_tagsids_to_links_TO_temp_tagsids FOREIGN KEY (TAG_ID) REFERENCES temp_tagsids (TAG_ID),
  CONSTRAINT FK_temp_tagsids_to_links_TO_SHORTENED_LINKS FOREIGN KEY (SHORTENED_LINK_ID) REFERENCES SHORTENED_LINKS (SHORTENED_LINK_ID)
)
  ENGINE =InnoDB;

#  tags with link in separate table

INSERT INTO temp_tagsids VALUES (1, 'nexus');
INSERT INTO temp_tagsids VALUES (2, 'nokia');
INSERT INTO temp_tagsids_to_links VALUES (1, 1);
INSERT INTO temp_tagsids_to_links VALUES (1, 2);
INSERT INTO temp_tagsids_to_links VALUES (3, 1);
INSERT INTO temp_tagsids_to_links VALUES (4, 1);
INSERT INTO temp_tagsids_to_links VALUES (5, 1);
INSERT INTO temp_tagsids_to_links VALUES (6, 1);
INSERT INTO temp_tagsids_to_links VALUES (5, 2);
INSERT INTO temp_tagsids_to_links VALUES (6, 2);


#
# TAGS AUTOSUGGEST queries
#


# --temp_tags--

SELECT
  tag_name,
  count(*)
FROM temp_tags
WHERE tag_name LIKE 'n%' AND SHORTENED_LINK_ID IN
                             (SELECT
                                SHORTENED_LINK_ID
                              FROM SHORTENED_LINKS
                              WHERE CAMPAIGN_ID IN (SELECT
                                                      campaign_id
                                                    FROM campaigns
                                                    WHERE account_id = 2))
GROUP BY tag_name
ORDER BY count(*) DESC
LIMIT 100;


# --temp_tags_whith_accountid--

SELECT
  tag_name,
  count(*)
FROM temp_tags_whith_accountid
WHERE ACCOUNT_ID = 2 AND tag_name LIKE 'n%'
GROUP BY tag_name
ORDER BY count(*) DESC
LIMIT 100;

#  tags in shortened_links single field

SELECT
  TAGS,
  count(*)
FROM shortened_links
WHERE TAGS LIKE 'n%' AND SHORTENED_LINK_ID IN
                         (SELECT
                            SHORTENED_LINK_ID
                          FROM SHORTENED_LINKS
                          WHERE CAMPAIGN_ID IN (SELECT
                                                  campaign_id
                                                FROM campaigns
                                                WHERE account_id = 2))
GROUP BY TAGS
ORDER BY count(*) DESC
LIMIT 100;


#
# LINKS SEARCHING
#

# tags_links

SELECT
  SHORTENED_LINK_ID,
  TAG_NAME
FROM temp_tags
WHERE tag_name = 'nexus' AND SHORTENED_LINK_ID IN (SELECT
                                                     SHORTENED_LINK_ID
                                                   FROM SHORTENED_LINKS
                                                   WHERE CAMPAIGN_ID IN (SELECT
                                                                           campaign_id
                                                                         FROM campaigns
                                                                         WHERE account_id = 2))
LIMIT 100;


# tags_links with account id

SELECT
  SHORTENED_LINK_ID,
  tag_name
FROM temp_tags_whith_accountid
WHERE tag_name = 'nexus' AND account_id = 2
LIMIT 100;

# tags as single field in links

SELECT
  SHORTENED_LINK_ID,
  TAGS
FROM SHORTENED_LINKS
WHERE TAGS LIKE ('% tag2') AND SHORTENED_LINK_ID IN (SELECT
                                                       SHORTENED_LINK_ID
                                                     FROM SHORTENED_LINKS
                                                     WHERE CAMPAIGN_ID IN (SELECT
                                                                             campaign_id
                                                                           FROM campaigns
                                                                           WHERE account_id = 2))
LIMIT 100;


# tags ids
explain SELECT
  SHORTENED_LINK_ID
FROM SHORTENED_LINKS
WHERE SHORTENED_LINK_ID IN (SELECT
                              shortened_link_id
                            FROM temp_tagsids_to_links
                            WHERE tag_id IN (SELECT
                                               TAG_ID
                                             FROM temp_tagsids
                                             WHERE TAG_NAME = 'nexus')) AND SHORTENED_LINK_ID IN (SELECT
                                                                                                    SHORTENED_LINK_ID
                                                                                                  FROM SHORTENED_LINKS
                                                                                                  WHERE CAMPAIGN_ID IN
                                                                                                        (SELECT
                                                                                                           campaign_id
                                                                                                         FROM campaigns
                                                                                                         WHERE
                                                                                                           account_id =
                                                                                                           2))
LIMIT 100;