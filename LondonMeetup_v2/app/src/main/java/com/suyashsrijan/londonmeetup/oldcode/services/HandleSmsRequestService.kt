package com.suyashsrijan.londonmeetup.services

import android.app.IntentService
import android.content.Intent
import android.support.v4.app.NotificationManagerCompat
import android.util.Log

import com.suyashsrijan.londonmeetup.utils.SmsUtils
import com.suyashsrijan.londonmeetup.utils.Utils

class HandleSmsRequestService : IntentService(HandleSmsRequestService::class.java.simpleName) {

    override fun onHandleIntent(intent: Intent?) {
        val action = intent!!.action
        if ("reject_req" == action) {
            NotificationManagerCompat.from(this).cancel(3141)
            val friendMobileNumber = intent.extras!!.getString("friend_number")
            try {
                SmsUtils.sendFriendRequestResult(applicationContext, friendMobileNumber, true)
            } catch (e: Exception) {
                Log.e(TAG, e.message)
            }

        } else if ("accept_req" == action) {
            NotificationManagerCompat.from(this).cancel(3141)
            val friendName = intent.extras!!.getString("friend_name")
            val friendMobileNumber = intent.extras!!.getString("friend_number")
            Utils.addFriendToList(applicationContext, friendName!!, friendMobileNumber!!)
            try {
                SmsUtils.sendFriendRequestResult(applicationContext, friendMobileNumber, true)
            } catch (e: Exception) {
                Log.e(TAG, e.message)
            }

        }
    }

    companion object {

        val TAG = "London Meetup [IS]"
    }
}
