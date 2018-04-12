package com.suyashsrijan.londonmeetup.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;

import com.google.android.gms.maps.model.LatLng;
import com.suyashsrijan.londonmeetup.models.Friend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SmsUtils {

    public static void sendFriendRequest(Context context, String mobileNumber) throws Exception {
        JSONObject friendObject = new JSONObject();
        friendObject.put("reqType", "add_friend");
        friendObject.put("from", PreferenceManager.getDefaultSharedPreferences(context).getString("displayName", "No name"));
        friendObject.put("number", PreferenceManager.getDefaultSharedPreferences(context).getString("mobileNumber", "0000000000"));
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(mobileNumber, null, friendObject.toString(), null, null);
    }

    public static void sendFriendRequestResult(Context context, String mobileNumber, boolean result) throws Exception {
        JSONObject friendReqResultObject = new JSONObject();
        friendReqResultObject.put("reqType", "add_friend_result");
        friendReqResultObject.put("from", PreferenceManager.getDefaultSharedPreferences(context).getString("displayName", "No name"));
        friendReqResultObject.put("number", PreferenceManager.getDefaultSharedPreferences(context).getString("mobileNumber", "0000000000"));
        friendReqResultObject.put("result", result);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(mobileNumber, null, friendReqResultObject.toString(), null, null);
    }

    public static void sendMyLocation(Context context, LatLng coordinates) throws Exception {
        JSONObject myLocationObject = new JSONObject();
        myLocationObject.put("reqType", "location_update");
        myLocationObject.put("from", PreferenceManager.getDefaultSharedPreferences(context).getString("displayName", "No name"));
        myLocationObject.put("lat", coordinates.latitude);
        myLocationObject.put("lon", coordinates.longitude);
        SmsManager smsManager = SmsManager.getDefault();

        ArrayList<Friend> friendArrayList = new ArrayList<>();
        String friendsListData = IOUtils.readFileString(context, "friends_list.json");
        JSONArray friendsList = new JSONArray(friendsListData);
        for (int i = 0; i < friendsList.length(); i++) {
            JSONObject friend = friendsList.getJSONObject(i);
            friendArrayList.add(new Friend(friend.getString("friend_name"), friend.getString("friend_number")));
        }

        for (Friend f : friendArrayList) {
            smsManager.sendTextMessage(f.getFriendMobileNumber(), null, myLocationObject.toString(), null, null);
        }
    }

    public static void sendMeetupLocation(Context context, LatLng coordinates) throws Exception {
        JSONObject myLocationObject = new JSONObject();
        myLocationObject.put("reqType", "meetup_location");
        myLocationObject.put("lat", coordinates.latitude);
        myLocationObject.put("lon", coordinates.longitude);
        SmsManager smsManager = SmsManager.getDefault();

        ArrayList<Friend> friendArrayList = new ArrayList<>();
        String friendsListData = IOUtils.readFileString(context, "friends_list.json");
        JSONArray friendsList = new JSONArray(friendsListData);
        for (int i = 0; i < friendsList.length(); i++) {
            JSONObject friend = friendsList.getJSONObject(i);
            friendArrayList.add(new Friend(friend.getString("friend_name"), friend.getString("friend_number")));
        }

        for (Friend f : friendArrayList) {
            smsManager.sendTextMessage(f.getFriendMobileNumber(), null, myLocationObject.toString(), null, null);
        }
    }
}
