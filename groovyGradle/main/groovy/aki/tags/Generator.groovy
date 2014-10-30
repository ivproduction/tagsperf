package aki.tags

import org.apache.commons.lang3.RandomStringUtils

/**
 * @author Alex Kirilov  27-10-14
 */
class Generator {
    int randomStringLength = 32
    String charset = (('a'..'z') + ('A'..'Z') + ('0'..'9')).join()

    def generate(){
        RandomStringUtils.random(randomStringLength, charset.toCharArray())
    }

    def generate(length){
        RandomStringUtils.random(length, charset.toCharArray())
    }
}
