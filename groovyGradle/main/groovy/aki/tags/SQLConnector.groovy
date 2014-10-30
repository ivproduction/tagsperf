package aki.tags

import groovy.sql.Sql

/**
 * @author Alex Kirilov  27-10-14
 */
class SQLConnector {
    def create() {
        def final ADDRESS = "jdbc:mysql://localhost:3306/post_master_dev"
        def final USERNAME = "post"
        def final PASSWD = "post"
        def final DRIVER = 'com.mysql.jdbc.Driver'
        return Sql.newInstance(ADDRESS, USERNAME, PASSWD, DRIVER)
    }
}
