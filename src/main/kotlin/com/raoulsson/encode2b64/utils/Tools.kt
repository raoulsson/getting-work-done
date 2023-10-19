package com.raoulsson.encode2b64.utils

import java.io.File

object Tools {

    fun cleanOutDir(outDir: String?) {
        outDir?.let {
            deleteDirectory(File(outDir))
            File(outDir).mkdirs()
        }
    }

    private fun deleteDirectory(dir: File) {
        if (dir.exists()) {
            dir.listFiles()?.forEach { file ->
                if (file.isDirectory) {
                    deleteDirectory(file)
                } else {
                    file.delete()
                }
            }
            dir.delete()
        }
    }

    fun debugPrint(content: String) {
        println("Debug out file: ")
        content.lines().forEach { line ->
            println(line)
        }
        println("End debug out full file")
    }

}