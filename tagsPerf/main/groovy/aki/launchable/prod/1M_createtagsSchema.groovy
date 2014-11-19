package aki.launchable

import aki.utils.ProdSQLConnector
import groovy.sql.Sql

def Sql masterDs = new ProdSQLConnector().createMaster();

try {
    def text = new File("tagsSchema.sql").text
    masterDs.execute(text)

    //filTagsTablewithTags
    TagsDics.taglist_ultra_short.each { tag ->
        masterDs.execute("INSERT IGNORE INTO TAGS (TAG_NAME) VALUES ($tag)");
    }

} finally {
    masterDs.close();
}
