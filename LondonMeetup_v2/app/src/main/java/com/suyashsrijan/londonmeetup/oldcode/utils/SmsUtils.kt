package com.suyashsrijan.londonmeetup.utils

import android.content.Context
import android.preference.PreferenceManager
import android.telephony.SmsManager

import com.google.android.gms.maps.model.LatLng
import com.suyashsrijan.londonmeetup.models.Friend

import org.json.JSONArray
import org.json.JSONObject

import java.util.ArrayList

object SmsUtils {

    @Throws(Exception::class)
    fun sendFriendRequest(context: Context, mobileNumber: String) {
        val friendObject = JSONObject()
        friendObject.put("reqType", "add_friend")
        friendObject.put("from", PreferenceManager.getDefaultSharedPreferences(context).getString("displayName", "No name"))
        friendObject.put("number", PreferenceManager.getDefaultSharedPreferences(context).getString("mobileNumber", "0000000000"))
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(mobileNumber, null, friendObject.toString(), null, null)
    }

    @Throws(Exception::class)
    fun sendFriendRequestResult(context: Context, mobileNumber: String, result: Boolean) {
        val friendReqResultObject = JSONObject()
        friendReqResultObject.put("reqType", "add_friend_result")
        friendReqResultObject.put("from", PreferenceManager.getDefaultSharedPreferences(context).getString("displayName", "No name"))
        friendReqResultObject.put("number", PreferenceManager.getDefaultSharedPreferences(context).getString("mobileNumber", "0000000000"))
        friendReqResultObject.put("result", result)
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(mobileNumber, null, friendReqResultObject.toString(), null, null)
    }

    @Throws(Exception::class)
    fun sendMyLocation(context: Context, coordinates: LatLng) {
        val myLocationObject = JSONObject()
        myLocationObject.put("reqType", "location_update")
        myLocationObject.put("from", PreferenceManager.getDefaultSharedPreferences(context).getString("displayName", "No name"))
        myLocationObject.put("lat", coordinates.latitude)
        myLocationObject.put("lon", coordinates.longitude)
        val smsManager = SmsManager.getDefault()

        val friendArrayList = ArrayList<Friend>()
        val friendsListData = IOUtils.readFileString(context, "friends_list.json")
        val friendsList = JSONArray(friendsListData)
        for (i in 0 until friendsList.length()) {
            val friend = friendsList.getJSONObject(i)
            friendArrayList.add(Friend(friend.getString("friend_name"), friend.getString("friend_number")))
        }

        for (f in friendArrayList) {
            smsManager.sendTextMessage(f.friendMobileNumber, null, myLocationObject.toString(), null, null)
        }
    }

    @Throws(Exception::class)
    fun sendMeetupLocation(context: Context, coordinates: LatLng) {
        val myLocationObject = JSONObject()
        myLocationObject.put("reqType", "meetup_location")
        myLocationObject.put("lat", coordinates.latitude)
        myLocationObject.put("lon", coordinates.longitude)
        val smsManager = SmsManager.getDefault()

        val friendArrayList = ArrayList<Friend>()
        val friendsListData = IOUtils.readFileString(context, "friends_list.json")
        val friendsList = JSONArray(friendsListData)
        for (i in 0 until friendsList.length()) {
            val friend = friendsList.getJSONObject(i)
            friendArrayList.add(Friend(friend.getString("friend_name"), friend.getString("friend_number")))
        }

        for (f in friendArrayList) {
            smsManager.sendTextMessage(f.friendMobileNumber, null, myLocationObject.toString(), null, null)
        }
    }
}
