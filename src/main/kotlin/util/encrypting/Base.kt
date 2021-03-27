package util.encrypting

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException

class Base { // украл отсюда: https://stackoverflow.com/questions/24338108/java-encrypt-string-with-existing-public-key-file/24343938
    @Throws(IOException::class)
    fun readFileBytes(filename: String?): ByteArray? {
        val path = Paths.get(filename)
        return Files.readAllBytes(path)
    }

    @Throws(IOException::class, NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun readPublicKey(filename: String?): PublicKey? {
        val publicSpec = X509EncodedKeySpec(readFileBytes(filename))
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(publicSpec)
    }

    @Throws(IOException::class, NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun readPrivateKey(filename: String?): PrivateKey? {
        val keySpec = PKCS8EncodedKeySpec(readFileBytes(filename))
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(keySpec)
    }

    @Throws(
            NoSuchAlgorithmException::class,
            NoSuchPaddingException::class,
            InvalidKeyException::class,
            IllegalBlockSizeException::class,
            BadPaddingException::class
    )
    fun encrypt(key: PublicKey?, plaintext: ByteArray?): ByteArray? {
        val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        return cipher.doFinal(plaintext)
    }

    @Throws(
            NoSuchAlgorithmException::class,
            NoSuchPaddingException::class,
            InvalidKeyException::class,
            IllegalBlockSizeException::class,
            BadPaddingException::class
    )
    fun decrypt(key: PrivateKey?, ciphertext: ByteArray?): ByteArray? {
        val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding")
        cipher.init(Cipher.DECRYPT_MODE, key)
        return cipher.doFinal(ciphertext)
    }
}