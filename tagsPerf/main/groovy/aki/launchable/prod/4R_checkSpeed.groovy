package aki.launchable.prod

import aki.utils.ProdSQLConnector
import groovy.sql.Sql

def Sql sql = new ProdSQLConnector().createStats();
def devEv = PerfQueries3.devEv;


def executedQueries = []
def executedQueriesNames = []

try {


    def workingAccountLinksMap = new LinkedHashMap();
    new File('accoutnLinksData.txt').eachLine { line ->
        if (!line.startsWith('#')) {
            def k = line.split(',')[0] as int;
            def v = (int) line.split(',')[1] as int;
            workingAccountLinksMap[k] = v;
        }
    }


    csv = new File("result.csv")
    csv.delete();
    csv.createNewFile()
    csv.append("id,name,query,duration,seq,state,numb_ops,sum_dur,avg_dur,sum_cpu,avg_cpu\r\n")

    def qtagName = sql.firstRow("SELECT  count(*), lt.TAG_ID, t.TAG_NAME FROM post_master" + devEv + ".LINK_TAGS lt JOIN post_master" + devEv + ".TAGS t ON t.TAG_ID=lt.TAG_ID" +
            " GROUP BY lt.TAG_ID ORDER BY count(*) DESC LIMIT 1;").TAG_NAME;

//    def qtagName = sql.firstRow("SELECT  count(*), lt.TAG_ID, t.TAG_NAME FROM post_master_dev.LINK_TAGS lt " +
//            "JOIN post_master_dev.TAGS t ON t.TAG_ID=lt.TAG_ID WHERE lt.ACCOUNT_ID=79764 GROUP BY lt.TAG_ID ORDER BY count(*) DESC LIMIT 1;").TAG_NAME


    sql.execute("set profiling_history_size=1000")
    sql.execute("SET profiling = 1;")


    workingAccountLinksMap.each { accountId, accountLinksCount ->
        PerfQueries3.queries.eachWithIndex {
            String query, index ->
                def queryR = query.replace("accountId", "" + accountId).replace("qtagName", "" + qtagName)
                sql.execute(queryR);
                executedQueries << queryR.replace("\n", " ");
                executedQueriesNames << PerfQueries3.queriesNames[index];
                println executedQueries.size() + "  ${PerfQueries3.queriesNames[index]} " + queryR.replace("\n", "");
        }
    }

    sql.execute("SET profiling = 0;")

    sql.eachRow("show profiles;") {
        row ->
            def queryId = row.Query_ID as int
            def duration = String.format("%f", row.Duration as double)

            def queryPerfRes = sql.firstRow("select min(seq) seq,state,count(*) numb_ops,\n" +
                    "round(sum(duration),5) sum_dur, round(avg(duration),5) avg_dur,\n" +
                    "round(sum(cpu_user),5) sum_cpu, round(avg(cpu_user),5) avg_cpu\n" +
                    "from information_schema.profiling\n" +
                    "where query_id = ${queryId}\n" +
                    "group by state\n" +
                    "order by seq;")

            def seq = queryPerfRes.seq
            def state = queryPerfRes.state
            def numb_ops = queryPerfRes.numb_ops
            def sum_dur = queryPerfRes.sum_dur
            def avg_dur = queryPerfRes.avg_dur
            def sum_cpu = queryPerfRes.sum_cpu
            def avg_cpu = queryPerfRes.avg_cpu

            if ((queryId - 1) % PerfQueries3.queries.size() == 0) {
                csv.append("\r\n");
            }

            csv.append("${queryId},${executedQueriesNames[queryId - 1]},${executedQueries[queryId - 1].replace("\n", " ").replace(",", "~")},${duration},${seq},${state},${numb_ops},${sum_dur},${avg_dur},${sum_cpu},${avg_cpu}\r\n");
    }


} finally {
    sql.close();
}