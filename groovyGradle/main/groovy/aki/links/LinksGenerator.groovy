package aki.links

import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

/**
 * @author Alex Kirilov  27-10-14
 */
class LinksGenerator {

//    private String apiKey = 'DAEC6743-61BF-4C40-A977-29231C0488D2';
    private String apiKey = 'bf3b9836-b2be-4cef-885b-c4bdd7034435';

    private String linkUrl = 'aki.d3.ru';
    private String shortLinkApiCall = "http://post.local:8888/api/shorten?longUrl=${linkUrl}&apiKey=${apiKey}&forceNew=true";
    private HTTPBuilder http = new HTTPBuilder(shortLinkApiCall);


    def addLink() {
        http.request(Method.POST, ContentType.ANY) { post ->
            //    println "Tweet response status: ${resp.statusLine}"
//    assert resp.statusLine.statusCode == 200
            println post
        }
    }
}
