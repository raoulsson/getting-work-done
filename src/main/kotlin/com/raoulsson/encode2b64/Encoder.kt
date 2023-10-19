package com.raoulsson.encode2b64

import com.raoulsson.encode2b64.utils.CryptIt
import com.raoulsson.encode2b64.utils.Tools
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class Encoder(
    private val password: String,
    private val salt: ByteArray,
    private val outDir: String,
    private val debugEnabled: Boolean = false,
) {

    init {
        if(password.isEmpty()) {
            throw IllegalArgumentException("Password must not be empty")
        }
        if(salt.isEmpty()) {
            throw IllegalArgumentException("Salt must not be empty")
        }
        if (password == "myPassword") {
            println("YOU ARE USING THE DEFAULT PASSWORD. MAKE SURE TO SET YOUR OWN PASSWORD AND SALT")
        }
    }

    fun encodeToBase64(absPath: String): String {
        if (!File(absPath).exists()) {
            throw IllegalArgumentException("Directory does not exist: $absPath")
        }

        val content = StringBuilder()
        val outFileBaseName = File(absPath).name

        Tools.cleanOutDir(outDir)

        if (File(absPath).isDirectory) {
            println("Encoding directory: $absPath")
            val baseDir = File(absPath).parent
            walkEncTree(absPath, content, baseDir)
        } else if (File(absPath).isFile) {
            println("Encoding file: $absPath")
            readFileToBuffer(File(absPath), "", content)
        } else {
            throw Exception("Not a file or directory: $absPath")
        }

        if (debugEnabled) {
            Tools.debugPrint(content.toString())
        }

        val encryptedBytes = CryptIt.encrypt(content.toString().toByteArray(Charsets.UTF_8), password, salt)

        val base64String = Base64.getEncoder().encodeToString(encryptedBytes)

        val outputFileName = "$outFileBaseName.b64"
        Files.write(Paths.get("$outDir/$outputFileName"), base64String.toByteArray(Charsets.UTF_8))
        return outputFileName
    }

    private fun walkEncTree(baseDir: String, content: StringBuilder, rootDirName: String) {
        val relativeSubDir = calcRelativeSubDir(baseDir, rootDirName)

        val currentDir = File(baseDir)
        currentDir.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                walkEncTree(file.absolutePath, content, rootDirName)
            } else if (!file.name.startsWith(".")) {
                readFileToBuffer(file, relativeSubDir, content)
            }
        }
    }

    private fun readFileToBuffer(file: File, relativeSubDir: String, content: StringBuilder) {
        val fileReader = FileReader(file)
        val bufferedReader = BufferedReader(fileReader)

        // Add the first line
        val metaLine = "${CryptIt.FILE_NAME_LINE}${file.name} " +
                CryptIt.DIR_NAME_LINE + relativeSubDir +
                System.lineSeparator()

        content.append(
            metaLine
        )

        var line = bufferedReader.readLine()

        while (line != null) {
            content.append(line)
            content.append(System.lineSeparator())
            line = bufferedReader.readLine()
        }
        bufferedReader.close()
    }

    private fun calcRelativeSubDir(baseDir: String, rootDirName: String): String {
        val relativeSubDir = baseDir.substring(rootDirName.length + 1)

        return if (relativeSubDir.contains("/")) {
            relativeSubDir.substring(relativeSubDir.indexOf("/") + 1)
        } else {
            ""
        }
    }

}