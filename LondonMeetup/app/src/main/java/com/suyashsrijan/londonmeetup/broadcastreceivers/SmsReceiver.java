package com.suyashsrijan.londonmeetup.broadcastreceivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.suyashsrijan.londonmeetup.activities.SettingsActivity;
import com.suyashsrijan.londonmeetup.enums.Constants;
import com.suyashsrijan.londonmeetup.services.HandleSmsRequestService;
import com.suyashsrijan.londonmeetup.utils.Utils;

import org.json.JSONObject;

public class SmsReceiver extends BroadcastReceiver {

    public static final String TAG = "London Meetup [SMS]";

    public void onReceive(Context context, Intent intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
                Object[] pdus = (Object[]) intent.getExtras().get("pdus");
                for (int i = 0; i < pdus.length; i++) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    try {
                        JSONObject request = new JSONObject(smsMessage.getMessageBody());
                        if (request.has("reqType")) {
                            if (PreferenceManager.getDefaultSharedPreferences(context).contains("displayName") && PreferenceManager.getDefaultSharedPreferences(context).contains("mobileNumber")) {
                                String requestType = request.getString("reqType");
                                if (requestType.equals("add_friend")) {
                                    String friendName = request.getString("from");
                                    String friendMobileNumber = request.getString("number");
                                    Intent acceptReqIntent = new Intent(context, HandleSmsRequestService.class)
                                            .setAction("accept_req")
                                            .putExtra("friend_name", friendName)
                                            .putExtra("friend_number", friendMobileNumber);
                                    Intent rejectReqIntent = new Intent(context, HandleSmsRequestService.class)
                                            .setAction("reject_req")
                                            .putExtra("friend_number", friendMobileNumber);
                                    PendingIntent acceptReqPendingIntent = PendingIntent.getService(context, 0,
                                            acceptReqIntent, PendingIntent.FLAG_ONE_SHOT);
                                    PendingIntent rejectReqPendingIntent = PendingIntent.getService(context, 0,
                                            rejectReqIntent, PendingIntent.FLAG_ONE_SHOT);
                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context.getApplicationContext(), "friend_reqs")
                                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                                            .setContentTitle("London Meetup")
                                            .setStyle(new NotificationCompat.BigTextStyle().bigText("You received a new friend request from " + friendName + " (" + friendMobileNumber + ")"))
                                            .setContentText("You have received a friend request...")
                                            .setAutoCancel(true)
                                            .addAction(0, "Accept", acceptReqPendingIntent)
                                            .addAction(0, "Reject", rejectReqPendingIntent);
                                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                                    NotificationManagerCompat.from(context).cancel(3141);
                                    notificationManager.notify(3141, mBuilder.build());
                                } else if (requestType.equals("add_friend_result")) {
                                    String friendName = request.getString("from");
                                    String friendMobileNumber = request.getString("number");
                                    boolean friendRequestResult = request.getBoolean("result");
                                    if (friendRequestResult) {
                                        Utils.addFriendToList(context, friendName, friendMobileNumber);
                                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context.getApplicationContext(), "friend_reqs")
                                                .setSmallIcon(android.R.drawable.ic_dialog_info)
                                                .setContentTitle("London Meetup")
                                                .setStyle(new NotificationCompat.BigTextStyle().bigText(friendName + " (" + friendMobileNumber + ") has accepted your friend request!"))
                                                .setContentText("Friend request accepted!")
                                                .setAutoCancel(true);
                                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                                        notificationManager.notify(3142, mBuilder.build());
                                    } else {
                                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context.getApplicationContext(), "friend_reqs")
                                                .setSmallIcon(android.R.drawable.ic_dialog_info)
                                                .setContentTitle("London Meetup")
                                                .setStyle(new NotificationCompat.BigTextStyle().bigText(friendName + " (" + friendMobileNumber + ") has rejected your friend request!"))
                                                .setContentText("Friend request rejected!")
                                                .setAutoCancel(true);
                                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                                        notificationManager.notify(3142, mBuilder.build());
                                    }
                                } else if (requestType.equals("meetup_location")) {
                                    double lat, lon;
                                    lat = request.getDouble("lat");
                                    lon = request.getDouble("lon");
                                    Intent meetupIntent = new Intent(Constants.INTENT_UPDATE_MEETUP_LOCATION);
                                    meetupIntent.putExtra("lat", lat);
                                    meetupIntent.putExtra("lon", lon);
                                    LocalBroadcastManager.getInstance(context).sendBroadcast(meetupIntent);
                                } else if (requestType.equals("location_update")) {
                                    double lat, lon;
                                    String friendName, friendNumber;
                                    lat = request.getDouble("lat");
                                    lon = request.getDouble("lon");
                                    friendName = request.getString("from");
                                    friendNumber = request.getString("number");
                                    Intent friendLocIntent = new Intent(Constants.INTENT_ADD_FRIEND_CURRENT_LOCATION);
                                    friendLocIntent.putExtra("lat", lat);
                                    friendLocIntent.putExtra("lon", lon);
                                    friendLocIntent.putExtra("name", friendName);
                                    friendLocIntent.putExtra("number", friendNumber);
                                    LocalBroadcastManager.getInstance(context).sendBroadcast(friendLocIntent);
                                }
                            } else {
                                Log.i(TAG, "Ignoring request as user has not set up their display name and number");
                                PendingIntent settingsActivityIntent = PendingIntent.getActivity(context, 0,
                                        new Intent(context, SettingsActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context.getApplicationContext(), "friend_reqs")
                                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                                        .setContentTitle("London Meetup")
                                        .setStyle(new NotificationCompat.BigTextStyle().bigText("You just received a request from a friend but in order to respond to friend requests and other kind of requests, you need to set up your display name and mobile number in Settings. Once you are done, you will be able to respond to future requests!"))
                                        .setContentIntent(settingsActivityIntent)
                                        .setContentText("You just received a request from a friend, but...")
                                        .setAutoCancel(true);
                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                                notificationManager.notify(3143, mBuilder.build());
                            }
                        } else {
                            Log.i(TAG, "Ignoring SMS as it does not contain reqType");
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, "Received a SMS, but it's not valid JSON, so ignoring");
                    }
                }
        }
    }

}
