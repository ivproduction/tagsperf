package aki.launchable.prod

import aki.utils.ProdSQLConnector
import groovy.sql.Sql

def Sql masterDs = new ProdSQLConnector().createMaster();

try {
    masterDs.executeUpdate("DELETE from LINK_TAGS")

} finally {
    masterDs.close();
}
