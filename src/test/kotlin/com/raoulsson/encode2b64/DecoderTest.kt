package com.raoulsson.encode2b64

import org.junit.jupiter.api.Test
import java.io.File

class DecoderTest {

    private val debugEnabled = false

    private val baseDir = File(".").absolutePath.substring(0, File(".").absolutePath.length - 1) +
            "src/test/resources/"
    private val outDir = File(".").absolutePath.substring(0, File(".").absolutePath.length - 1) +
            "src/test/resources/out/"
    private val password = "myPassword"
    private val salt: ByteArray = byteArrayOf(
        0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10
    )

    @Test
    fun decodeFileExample() {
        val encodedFile = "test-sources/decode/file-test.txt.b64"
        val absFilePath = baseDir + encodedFile
        val decoder = Decoder(password, salt, outDir, debugEnabled = debugEnabled)
        val decFromBase64 = decoder.decodeFromBase64(absFilePath)
        println("File converted from Base64 and decrypted and saved to: $decFromBase64")
        assert(true)
    }

    @Test
    fun decodeDirectoryExample() {
        val encodedFileName = "test-sources/decode/tree-test.b64"
        val absFilePath = baseDir + encodedFileName
        val decoder = Decoder(password, salt, outDir, debugEnabled = debugEnabled)
        val decFromBase64 = decoder.decodeFromBase64(absFilePath)
        println("File converted from Base64 and decrypted and saved to: $decFromBase64")
        assert(true)
    }

    @Test
    fun decodeDirectoryNoMetaDataExample() {
        val encodedFileName = "test-sources/decode/tree-test-no-meta.txt.b64"
        val absFilePath = baseDir + encodedFileName
        val decoder = Decoder(password, salt, outDir, debugEnabled = debugEnabled)
        val decFromBase64 = decoder.decodeFromBase64(absFilePath)
        println("File converted from Base64 and decrypted and saved to: $decFromBase64")
        assert(true)
    }

    @Test
    fun decodeFileNoMetaDataExample() {
        val encodedFile = "test-sources/decode/file-test-no-meta.txt.b64"
        val absFilePath = baseDir + encodedFile
        val decoder = Decoder(password, salt, outDir, debugEnabled = debugEnabled)
        val decFromBase64 = decoder.decodeFromBase64(absFilePath)
        println("File converted from Base64 and decrypted and saved to: $decFromBase64")
        assert(true)
    }

}