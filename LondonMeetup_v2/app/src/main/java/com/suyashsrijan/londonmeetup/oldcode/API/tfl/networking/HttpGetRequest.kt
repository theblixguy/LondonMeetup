package com.suyashsrijan.londonmeetup.API.tfl.networking

import android.os.AsyncTask

import com.suyashsrijan.londonmeetup.API.tfl.interfaces.HttpGetResultCallback

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HttpGetRequest(callback: HttpGetResultCallback) : AsyncTask<String, Void, String>() {

    private var delegate: HttpGetResultCallback? = null
    private var exception: Exception? = null

    init {
        this.delegate = callback
    }

    override fun doInBackground(vararg params: String): String {
        val urlString = params[0]
        var inputLine: String
        var result = ""
        try {
            val myUrl = URL(urlString)
            val connection = myUrl.openConnection() as HttpURLConnection
            connection.requestMethod = REQUEST_METHOD
            connection.readTimeout = READ_TIMEOUT
            connection.connectTimeout = CONNECTION_TIMEOUT
            connection.connect()

            val streamReader = InputStreamReader(connection.inputStream)
            val reader = BufferedReader(streamReader)
            val stringBuilder = StringBuilder()
            reader.lines().forEach { line ->
                stringBuilder.append(line)
            }
            /*while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine)
            }*/
            reader.close()
            streamReader.close()
            result = stringBuilder.toString()
            return result
        } catch (e: Exception) {
            exception = e
        }

        return result
    }

    override fun onPostExecute(result: String) {
        if (exception != null) {
            delegate!!.error(exception!!)
        } else {
            delegate!!.response(result)
        }
    }

    companion object {

        val REQUEST_METHOD = "GET"
        val READ_TIMEOUT = 15000
        val CONNECTION_TIMEOUT = 15000
    }
}