package com.suyashsrijan.londonmeetup.utils


import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.v7.app.AlertDialog

import com.suyashsrijan.londonmeetup.BuildConfig
import com.suyashsrijan.londonmeetup.R

import org.json.JSONArray
import org.json.JSONObject

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern

object Utils {

    fun openAppSettings(context: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun getTimeToArriveInMinutes(dateTime: String): Int {
        val arrivalTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK)
        arrivalTimeFormat.timeZone = TimeZone.getTimeZone("UTC")
        val arrivalTime: Date
        try {
            arrivalTime = arrivalTimeFormat.parse(dateTime)
        } catch (e: ParseException) {
            return -1
        }

        val diff = arrivalTime.time - Calendar.getInstance().timeInMillis
        return if (TimeUnit.MILLISECONDS.toMinutes(diff).toInt() < 1) {
            0
        } else if (TimeUnit.MILLISECONDS.toMinutes(diff).toInt() == 1) {
            1
        } else
            TimeUnit.MILLISECONDS.toMinutes(diff).toInt()
    }

    private fun isFloat(floatStr: String): Boolean {
        try {
            val f = java.lang.Float.parseFloat(floatStr)
            return true
        } catch (e: NumberFormatException) {
            return false
        }

    }

    fun isValidCoordinates(latitudeStr: String, longitudeStr: String): Boolean {
        if (isFloat(latitudeStr) && isFloat(latitudeStr)) {
            val latitude = java.lang.Float.parseFloat(latitudeStr)
            val longitude = java.lang.Float.parseFloat(longitudeStr)
            return latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180
        } else
            return false
    }

    fun isValidUKMobileNumber(mobileNumber: String): Boolean {
        val phoneNumberPattern = Pattern.compile("^(07\\d{8,12}|447\\d{7,11})$")
        val match = phoneNumberPattern.matcher(mobileNumber)
        return match.matches()
    }

    fun isValidName(name: String): Boolean {
        if (name.length < 2 || name.length > 35) {
            return false
        }
        val namePattern = Pattern.compile("^[\\p{L} ,.'-]*$")
        val match = namePattern.matcher(name)
        return match.matches()
    }

    fun addFriendToList(context: Context, friendName: String, friendNumber: String) {
        try {
            val friend = JSONObject()
            val friendsList = JSONArray(IOUtils.readFileString(context, "friends_list.json"))
            friend.put("friend_name", friendName)
            friend.put("friend_number", friendNumber)
            friendsList.put(friend)
            IOUtils.writeFileString(context, friendsList.toString(), "friends_list.json")
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    fun showErrorDialog(context: Context, title: String, message: String) {
        val builder = AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Close") { dialogInterface, i -> dialogInterface.dismiss() }
        builder.show()
    }
}
