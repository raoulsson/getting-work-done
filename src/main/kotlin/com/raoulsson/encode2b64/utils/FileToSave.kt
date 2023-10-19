package com.raoulsson.encode2b64.utils

class FileToSave(
    val fileName: String,
    val subDir: String,
    val content: String
) {

    override fun toString(): String {
        return "FileWithContext(fileName='$fileName', subDir='$subDir', content='${
            content.replace(
                System.lineSeparator(),
                " "
            ).take(50)
        }...')"
    }
}