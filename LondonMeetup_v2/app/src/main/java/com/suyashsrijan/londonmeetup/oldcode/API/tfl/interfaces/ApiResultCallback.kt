package com.suyashsrijan.londonmeetup.API.tfl.interfaces

import android.content.Context

import java.util.ArrayList

interface ApiResultCallback<T> {
    fun response(context: Context, output: ArrayList<T>)
    fun error(context: Context, exception: Exception)
}
