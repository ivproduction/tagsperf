package aki.utils

import groovy.sql.Sql

/**
 * @author Alex Kirilov  27-10-14
 */
class SQLConnector {
    def masterUrl = "jdbc:mysql://localhost:3306/post_master_dev"
    def masterUser = "post"
    def masterPass = "post"

    def statsUrl = "jdbc:mysql://localhost:3306/post_stat_dev"
    def statsUser = "post"
    def statsPass = "post"

    Sql createMaster() {
        def final ADDRESS = masterUrl + "?allowMultiQueries=true"
        Sql.newInstance(ADDRESS, masterUser, masterPass, 'com.mysql.jdbc.Driver')
    }

    Sql createStats() {
        def final ADDRESS = statsUrl + "?allowMultiQueries=true"
        Sql.newInstance(ADDRESS, statsUser, statsPass, 'com.mysql.jdbc.Driver')
    }
}
