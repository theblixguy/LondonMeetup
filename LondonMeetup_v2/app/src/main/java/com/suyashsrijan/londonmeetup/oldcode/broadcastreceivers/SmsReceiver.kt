package com.suyashsrijan.londonmeetup.broadcastreceivers

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.provider.Telephony
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.LocalBroadcastManager
import android.telephony.SmsMessage
import android.util.Log

import com.suyashsrijan.londonmeetup.activities.SettingsActivity
import com.suyashsrijan.londonmeetup.enums.Constants
import com.suyashsrijan.londonmeetup.services.HandleSmsRequestService
import com.suyashsrijan.londonmeetup.utils.Utils

import org.json.JSONObject

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent.action) {
            val pdus = intent.extras!!.get("pdus") as Array<Any>
            for (i in pdus.indices) {
                val smsMessage = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                try {
                    val request = JSONObject(smsMessage.messageBody)
                    if (request.has("reqType")) {
                        if (PreferenceManager.getDefaultSharedPreferences(context).contains("displayName") && PreferenceManager.getDefaultSharedPreferences(context).contains("mobileNumber")) {
                            val requestType = request.getString("reqType")
                            if (requestType == "add_friend") {
                                val friendName = request.getString("from")
                                val friendMobileNumber = request.getString("number")
                                val acceptReqIntent = Intent(context, HandleSmsRequestService::class.java)
                                        .setAction("accept_req")
                                        .putExtra("friend_name", friendName)
                                        .putExtra("friend_number", friendMobileNumber)
                                val rejectReqIntent = Intent(context, HandleSmsRequestService::class.java)
                                        .setAction("reject_req")
                                        .putExtra("friend_number", friendMobileNumber)
                                val acceptReqPendingIntent = PendingIntent.getService(context, 0,
                                        acceptReqIntent, PendingIntent.FLAG_ONE_SHOT)
                                val rejectReqPendingIntent = PendingIntent.getService(context, 0,
                                        rejectReqIntent, PendingIntent.FLAG_ONE_SHOT)
                                val mBuilder = NotificationCompat.Builder(context.applicationContext, "friend_reqs")
                                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                                        .setContentTitle("London Meetup")
                                        .setStyle(NotificationCompat.BigTextStyle().bigText("You received a new friend request from $friendName ($friendMobileNumber)"))
                                        .setContentText("You have received a friend request...")
                                        .setAutoCancel(true)
                                        .addAction(0, "Accept", acceptReqPendingIntent)
                                        .addAction(0, "Reject", rejectReqPendingIntent)
                                val notificationManager = NotificationManagerCompat.from(context)
                                NotificationManagerCompat.from(context).cancel(3141)
                                notificationManager.notify(3141, mBuilder.build())
                            } else if (requestType == "add_friend_result") {
                                val friendName = request.getString("from")
                                val friendMobileNumber = request.getString("number")
                                val friendRequestResult = request.getBoolean("result")
                                if (friendRequestResult) {
                                    Utils.addFriendToList(context, friendName, friendMobileNumber)
                                    val mBuilder = NotificationCompat.Builder(context.applicationContext, "friend_reqs")
                                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                                            .setContentTitle("London Meetup")
                                            .setStyle(NotificationCompat.BigTextStyle().bigText("$friendName ($friendMobileNumber) has accepted your friend request!"))
                                            .setContentText("Friend request accepted!")
                                            .setAutoCancel(true)
                                    val notificationManager = NotificationManagerCompat.from(context)
                                    notificationManager.notify(3142, mBuilder.build())
                                } else {
                                    val mBuilder = NotificationCompat.Builder(context.applicationContext, "friend_reqs")
                                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                                            .setContentTitle("London Meetup")
                                            .setStyle(NotificationCompat.BigTextStyle().bigText("$friendName ($friendMobileNumber) has rejected your friend request!"))
                                            .setContentText("Friend request rejected!")
                                            .setAutoCancel(true)
                                    val notificationManager = NotificationManagerCompat.from(context)
                                    notificationManager.notify(3142, mBuilder.build())
                                }
                            } else if (requestType == "meetup_location") {
                                val lat: Double
                                val lon: Double
                                lat = request.getDouble("lat")
                                lon = request.getDouble("lon")
                                val meetupIntent = Intent(Constants.INTENT_UPDATE_MEETUP_LOCATION)
                                meetupIntent.putExtra("lat", lat)
                                meetupIntent.putExtra("lon", lon)
                                LocalBroadcastManager.getInstance(context).sendBroadcast(meetupIntent)
                            } else if (requestType == "location_update") {
                                val lat: Double
                                val lon: Double
                                val friendName: String
                                val friendNumber: String
                                lat = request.getDouble("lat")
                                lon = request.getDouble("lon")
                                friendName = request.getString("from")
                                friendNumber = request.getString("number")
                                val friendLocIntent = Intent(Constants.INTENT_ADD_FRIEND_CURRENT_LOCATION)
                                friendLocIntent.putExtra("lat", lat)
                                friendLocIntent.putExtra("lon", lon)
                                friendLocIntent.putExtra("name", friendName)
                                friendLocIntent.putExtra("number", friendNumber)
                                LocalBroadcastManager.getInstance(context).sendBroadcast(friendLocIntent)
                            }
                        } else {
                            Log.i(TAG, "Ignoring request as user has not set up their display name and number")
                            val settingsActivityIntent = PendingIntent.getActivity(context, 0,
                                    Intent(context, SettingsActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
                            val mBuilder = NotificationCompat.Builder(context.applicationContext, "friend_reqs")
                                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                                    .setContentTitle("London Meetup")
                                    .setStyle(NotificationCompat.BigTextStyle().bigText("You just received a request from a friend but in order to respond to friend requests and other kind of requests, you need to set up your display name and mobile number in Settings. Once you are done, you will be able to respond to future requests!"))
                                    .setContentIntent(settingsActivityIntent)
                                    .setContentText("You just received a request from a friend, but...")
                                    .setAutoCancel(true)
                            val notificationManager = NotificationManagerCompat.from(context)
                            notificationManager.notify(3143, mBuilder.build())
                        }
                    } else {
                        Log.i(TAG, "Ignoring SMS as it does not contain reqType")
                    }
                } catch (ex: Exception) {
                    Log.e(TAG, "Received a SMS, but it's not valid JSON, so ignoring")
                }

            }
        }
    }

    companion object {

        val TAG = "London Meetup [SMS]"
    }

}
