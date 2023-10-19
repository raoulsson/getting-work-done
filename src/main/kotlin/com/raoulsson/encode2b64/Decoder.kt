package com.raoulsson.encode2b64

import com.raoulsson.encode2b64.utils.CryptIt
import com.raoulsson.encode2b64.utils.FileToSave
import com.raoulsson.encode2b64.utils.Tools
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class Decoder(
    private val password: String,
    private val salt: ByteArray,
    private val outDir: String,
    private val debugEnabled: Boolean = false
) {

    init {
        if (password == "myPassword") {
            println("YOU ARE USING THE DEFAULT PASSWORD. MAKE SURE TO SET YOUR OWN PASSWORD AND SALT")
        }
    }

    fun decodeFromBase64(absFilePath: String): String {
        if (!File(absFilePath).exists() && !File(absFilePath).isDirectory) {
            throw IllegalArgumentException("Base directory does not exist or is not a directory: $absFilePath")
        }

        Tools.cleanOutDir(outDir)

        val base64String = String(Files.readAllBytes(Paths.get(absFilePath)), Charsets.UTF_8)
        val decodedBytes = Base64.getDecoder().decode(base64String)
        val decryptedBytes = CryptIt.decrypt(decodedBytes, password, salt)

        val content = String(decryptedBytes)

        if (debugEnabled) {
            Tools.debugPrint(content)
        }

        val filesToCreate: MutableList<FileToSave> = mutableListOf()

        var fileContentAcc = StringBuilder()

        var fileName: String? = null
        var dirName: String? = null

        for (line in content.lines()) {
            val dirAndFile: Pair<String?, String?>? = matchMetaLineOrNull(line)

            if (dirAndFile != null) {
                if (fileContentAcc.isNotEmpty()) {
                    filesToCreate.add(FileToSave(fileName!!, dirName!!, fileContentAcc.toString()))
                    fileContentAcc = StringBuilder()
                }
                fileName = dirAndFile.first
                dirName = dirAndFile.second
            } else {
                fileContentAcc.append(line)
                fileContentAcc.append(System.lineSeparator())
            }
        }

        // Handle last file
        if (fileContentAcc.isNotEmpty() && fileName != null && dirName != null) {
            filesToCreate.add(FileToSave(fileName, dirName, fileContentAcc.toString()))
        }

        // If there is no meta data, create a file with the full content
        if (filesToCreate.isEmpty()) {
            var outFileName = File(absFilePath).name
            if (outFileName.endsWith(".b64")) {
                outFileName = outFileName.substring(0, outFileName.length - 4)
            }
            filesToCreate.add(FileToSave(outFileName, "", content))
        }

        if (debugEnabled) {
            println("Files to create:")
            filesToCreate.forEach(::println)
        }

        createDirsAndSaveFiles(outDir, filesToCreate)

        return outDir
    }

    private fun createDirsAndSaveFiles(outDirBasePath: String, filesToCreate: MutableList<FileToSave>) {
        filesToCreate.forEach { fileWithContext ->
            createDirs(fileWithContext.subDir, outDirBasePath)
            Files.write(
                Paths.get("$outDirBasePath/${fileWithContext.subDir}/${fileWithContext.fileName}"),
                fileWithContext.content.toByteArray(Charsets.UTF_8)
            )
        }
    }

    private fun createDirs(subDirs: String?, outDirFullPath: String?) {
        if (debugEnabled) {
            println("Creating dirs: $outDirFullPath$subDirs")
        }
        Files.createDirectories(Paths.get("$outDirFullPath$subDirs"))
    }

    private fun matchMetaLineOrNull(line: String): Pair<String?, String>? {
        if (line.startsWith(CryptIt.FILE_NAME_LINE) && !line.startsWith(".")) {
            val partOne = line.substring(0, line.indexOf(CryptIt.DIR_NAME_LINE) - 1)
            val partTwo = line.substring(line.indexOf(CryptIt.DIR_NAME_LINE))
            val fileName = partOne.substring(CryptIt.FILE_NAME_LINE.length).trim()
            val dirName = partTwo.substring(CryptIt.DIR_NAME_LINE.length).trim()
            return Pair(fileName, dirName)
        }
        return null
    }

}