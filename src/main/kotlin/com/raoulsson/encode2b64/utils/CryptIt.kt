package com.raoulsson.encode2b64.utils

import java.security.Key
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object CryptIt {

    const val FILE_NAME_LINE = "//meta ENC_B64_FILE_NAME: "
    const val DIR_NAME_LINE = "//meta ENC_B64_DIR_NAME: "

    fun encrypt(input: ByteArray, password: String, salt: ByteArray): ByteArray {
        val secretKey: Key = generateSecretKey(password, salt)
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return cipher.doFinal(input)
    }

    fun decrypt(input: ByteArray, password: String, salt: ByteArray): ByteArray {
        val secretKey: Key = generateSecretKey(password, salt)
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        return cipher.doFinal(input)
    }

    private fun generateSecretKey(password: String, salt: ByteArray): Key {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val keySpec: KeySpec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
        val secretKey = factory.generateSecret(keySpec)
        return SecretKeySpec(secretKey.encoded, "AES")
    }
}