package aki.benchmark

import aki.tags.Generator
import aki.tags.LinkTagsManager
import aki.tags.SQLConnector
import groovy.sql.Sql

LinkTagsManager.getInstance()

//LinkTagsManager.getInstance().deleteAllLinks();
//LinkTagsManager.createRandomTags(10000);

def Sql sql = new SQLConnector().create();

/*** autosuggest queries**/
def generator = new Generator()
int to = 2
def strings = new String[to];
def begSearchCharacter = 'i'
strings.eachWithIndex { it, index ->
    begSearchCharacter = begSearchCharacter + generator.generate(1);
    strings[index] = begSearchCharacter + '%'
}

println 'Start of temp_tags_whith_accountid'
(1..10).each {
    def iterDurationResult = new long[to]
    long iterBegTime;
    def begTime = System.currentTimeMillis()
    (1..to).eachWithIndex { it1, i ->
        iterBegTime = System.currentTimeMillis();
        def someTag = LinkTagsManager.preDefinedTags[i % LinkTagsManager.preDefinedTags.size() - 1]
        def randomTagPrefix = "'" + someTag[0..1] + '%' + "'";
        strings[i] = randomTagPrefix;
        sql.execute("select SQL_NO_CACHE distinct(tag_name) from temp_tags_whith_accountid where ACCOUNT_ID=2 and tag_name like ${randomTagPrefix} limit 100")
        iterDurationResult[i] = System.currentTimeMillis() - iterBegTime;
    }
    totalDuration = System.currentTimeMillis() - begTime;

    iterDurationResult.eachWithIndex { long entry, int i ->
        println i + ' ' + strings[i] + ' result: ' + iterDurationResult[i];
    }
    println 'Total -----> ' + totalDuration;
}
println 'End of of temp_tags_whith_accountid'




println 'Start of temp_tags'
(1..10).each {
    def iterDurationResult = new long[to]
    long iterBegTime;
    def begTime = System.currentTimeMillis()
    (1..to).eachWithIndex { it1, i ->
        iterBegTime = System.currentTimeMillis();
        def someTag = LinkTagsManager.preDefinedTags[i % LinkTagsManager.preDefinedTags.size() - 1]
        def randomTagPrefix = "'" + someTag[0..2] + '%' + "'";
        strings[i] = randomTagPrefix;
        sql.execute("select SQL_NO_CACHE distinct(tag_name) from temp_tags where tag_name like ${randomTagPrefix} " +
                "and SHORTENED_LINK_ID in (select SHORTENED_LINK_ID from SHORTENED_LINKS where CAMPAIGN_ID in (select campaign_id from campaigns where account_id=2)) limit 100")
        iterDurationResult[i] = System.currentTimeMillis() - iterBegTime;
    }
    totalDuration = System.currentTimeMillis() - begTime;

    iterDurationResult.eachWithIndex { long entry, int i ->
        println i + ' ' + strings[i] + ' result: ' + iterDurationResult[i];
    }
    println 'total ' + totalDuration;
}
println 'End of of temp_tags'
