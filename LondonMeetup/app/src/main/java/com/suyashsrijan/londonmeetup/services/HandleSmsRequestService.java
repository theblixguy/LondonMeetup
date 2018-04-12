package com.suyashsrijan.londonmeetup.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.suyashsrijan.londonmeetup.utils.SmsUtils;
import com.suyashsrijan.londonmeetup.utils.Utils;

public class HandleSmsRequestService extends IntentService {

    public static final String TAG = "London Meetup [IS]";

    public HandleSmsRequestService() {
        super(HandleSmsRequestService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if ("reject_req".equals(action)) {
            NotificationManagerCompat.from(this).cancel(3141);
            String friendMobileNumber = intent.getExtras().getString("friend_number");
            try {
                SmsUtils.sendFriendRequestResult(getApplicationContext(), friendMobileNumber, true);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if ("accept_req".equals(action)) {
            NotificationManagerCompat.from(this).cancel(3141);
            String friendName = intent.getExtras().getString("friend_name");
            String friendMobileNumber = intent.getExtras().getString("friend_number");
            Utils.addFriendToList(getApplicationContext(), friendName, friendMobileNumber);
            try {
                SmsUtils.sendFriendRequestResult(getApplicationContext(), friendMobileNumber, true);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}
