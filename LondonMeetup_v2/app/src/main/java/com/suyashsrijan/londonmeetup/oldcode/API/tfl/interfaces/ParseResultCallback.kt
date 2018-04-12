package com.suyashsrijan.londonmeetup.API.tfl.interfaces

import java.util.ArrayList

interface ParseResultCallback<T> {
    fun response(output: ArrayList<T>)
    fun error(exception: Exception)
}
