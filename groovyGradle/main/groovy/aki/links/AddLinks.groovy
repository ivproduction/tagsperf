package aki.links

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import static groovyx.net.http.ContentType.ANY

apiKey = 'DAEC6743-61BF-4C40-A977-29231C0488D2';
linkUrl = 'aki.d3.ru';
shortLinkApiCall = "http://post.local:8888/api/shorten?longUrl=${linkUrl}&apiKey=${apiKey}&forceNew=true";


//def http = new HTTPBuilder(shortLinkApiCall);

//http.request(Method.POST, ANY) { post ->
////    println "Tweet response status: ${resp.statusLine}"
////    assert resp.statusLine.statusCode == 200
//    println post
//}
//
def http = new HTTPBuilder( 'http://ajax.googleapis.com' )

// perform a GET request, expecting JSON response data
http.request( Method.GET, ANY ) {
    uri.path = '/ajax/services/search/web'
    uri.query = [ v:'1.0', q: 'Calvin and Hobbes' ]

    headers.'User-Agent' = 'Mozilla/5.0 Ubuntu/8.10 Firefox/3.0.4'

    // response handler for a success response code:
    response.success = { resp, json ->
        println resp.status

        // parse the JSON response object:
        json.responseData.results.each {
            println "  ${it.titleNoFormatting} : ${it.visibleUrl}"
        }
    }

    // handler for any failure status code:
    response.failure = { resp ->
        println "Unexpected error: ${resp.status} : ${resp.statusLine.reasonPhrase}"
    }
}