package com.suyashsrijan.londonmeetup.utils

import android.content.Context

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

object IOUtils {

    @Throws(IOException::class)
    fun writeFileString(context: Context, fileContents: String, fileName: String) {
        val out = FileWriter(File(context.filesDir, fileName))
        out.write(fileContents)
        out.close()
    }

    @Throws(Exception::class)
    fun readFileString(context: Context, fileName: String): String {
        val stringBuilder = StringBuilder()
        var line: String
        val inputReader: BufferedReader
        val file = File(context.filesDir, fileName)
        if (!file.exists()) {
            writeFileString(context, "[]", fileName)
        }
        inputReader = BufferedReader(FileReader(file))
        inputReader.lines().forEach { line ->
            stringBuilder.append(line)
        }
        /*while ((line = inputReader.readLine()) != null) {
            stringBuilder.append(line)
        }*/
        return stringBuilder.toString()
    }
}
