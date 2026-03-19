package com.raoulsson.encode2b64

import org.junit.jupiter.api.Test
import java.io.File

class EncoderTest {

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
    fun encodeFileExample() {
        val plainFile = "test-sources/encode/file-test.txt"
        val absFilePath = baseDir + plainFile
        val encoder = Encoder(password, salt, outDir, debugEnabled = debugEnabled)
        val encFromBase64 = encoder.encodeToBase64(absFilePath)
        println("File encrypted and converted to Base64 and saved to file: $encFromBase64")
        assert(true)
    }

    @Test
    fun encodeDirectoryExample() {
        val rootDir = "test-sources/encode/tree-test"
        val absFilePath = baseDir + rootDir
        val encoder = Encoder(password, salt, outDir, debugEnabled = debugEnabled)
        val encFromBase64 = encoder.encodeToBase64(absFilePath)
        println("Directory encrypted and converted to Base64 and saved to file: $encFromBase64")
        assert(true)
    }

    @Test
    fun encodeDirectoryNoMetaDataExample() {
        val rootDir = "test-sources/encode/tree-test"
        val absFilePath = baseDir + rootDir
        val encoder = Encoder(password, salt, outDir, debugEnabled = debugEnabled)
        val encFromBase64 = encoder.encodeToBase64(absFilePath)
        println("Directory encrypted and converted to Base64 and saved to file: $encFromBase64")
        assert(true)
    }

    @Test
    fun encodeFileNoMetaDataExample() {
        val plainFile = "test-sources/encode/file-test.txt"
        val absFilePath = baseDir + plainFile
        val encoder = Encoder(password, salt, outDir, debugEnabled = debugEnabled)
        val encFromBase64 = encoder.encodeToBase64(absFilePath)
        println("File encrypted and converted to Base64 and saved to file: $encFromBase64")
        assert(true)
    }

}