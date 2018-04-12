package com.suyashsrijan.londonmeetup.API.tfl.interfaces

interface HttpGetResultCallback {
    fun response(output: String)
    fun error(exception: Exception)
}
