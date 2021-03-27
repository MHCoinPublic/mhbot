package util.encrypting

import util.getConfig
import java.util.*

class Main { // кодируем и декодируем полученный результат в base64
    fun base64Encrypt(words : String): String? {
        var base64 = ""
        try {
            val publicKey = Base().readPublicKey(getConfig()?.keys?.public)
            val message = words.toByteArray(charset("UTF8"))
            val secret = Base().encrypt(publicKey, message)
            base64 = Base64.getEncoder().encodeToString(secret)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return base64
    }

    fun base64Decrypt(word : String?): String? {
        var base64 = ""
        try {
            val privateKey = Base().readPrivateKey(getConfig()?.keys?.private)
            val words = Base64.getDecoder().decode(word)
            val secret = Base().decrypt(privateKey, words)
            base64 = secret?.let { String(it) }.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return base64
    }
}