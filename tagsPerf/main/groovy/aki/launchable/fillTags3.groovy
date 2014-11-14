package aki.launchable

/**
 * @author Anton Bernatski
 */

def file = new File("tags.sql")
file << "INSERT IGNORE INTO TAGS (TAG_NAME) VALUES\n"
TagsDics.taglist_ultra_short.each { tag ->
    file << "('$tag'),\n"
}
