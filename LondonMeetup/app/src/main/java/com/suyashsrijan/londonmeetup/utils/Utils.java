package com.suyashsrijan.londonmeetup.utils;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.suyashsrijan.londonmeetup.BuildConfig;
import com.suyashsrijan.londonmeetup.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static void openAppSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static int getTimeToArriveInMinutes(String dateTime) {
        SimpleDateFormat arrivalTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
        arrivalTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date arrivalTime;
        try {
            arrivalTime = arrivalTimeFormat.parse(dateTime);
        } catch (ParseException e) {
            return -1;
        }
        long diff = arrivalTime.getTime() - Calendar.getInstance().getTimeInMillis();
        if ((int) TimeUnit.MILLISECONDS.toMinutes(diff) < 1) {
            return 0;
        } else if ((int) TimeUnit.MILLISECONDS.toMinutes(diff) == 1) {
            return 1;
        } else return (int) TimeUnit.MILLISECONDS.toMinutes(diff);
    }

    private static boolean isFloat(String floatStr) {
            try {
                float f = Float.parseFloat(floatStr);
                return true;
            } catch(NumberFormatException e) {
                return false;
            }
    }

    public static boolean isValidCoordinates(String latitudeStr, String longitudeStr) {
        if (isFloat(latitudeStr) && isFloat(latitudeStr)) {
            float latitude = Float.parseFloat(latitudeStr);
            float longitude = Float.parseFloat(longitudeStr);
            return (latitude >= -90 && latitude <= 90) && (longitude >= -180 && longitude <= 180);
        } else return false;
    }

    public static boolean isValidUKMobileNumber(String mobileNumber) {
        Pattern phoneNumberPattern = Pattern.compile("^(07\\d{8,12}|447\\d{7,11})$");
        Matcher match = phoneNumberPattern.matcher(mobileNumber);
        return match.matches();
    }

    public static boolean isValidName(String name) {
        if (name.length() < 2 || name.length() > 35) { return false; }
        Pattern namePattern = Pattern.compile("^[\\p{L} ,.'-]*$");
        Matcher match = namePattern.matcher(name);
        return match.matches();
    }

    public static void addFriendToList(Context context, String friendName, String friendNumber) {
        try {
            JSONObject friend = new JSONObject();
            JSONArray friendsList = new JSONArray(IOUtils.readFileString(context, "friends_list.json"));
            friend.put("friend_name", friendName);
            friend.put("friend_number", friendNumber);
            friendsList.put(friend);
            IOUtils.writeFileString(context, friendsList.toString(), "friends_list.json");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void showErrorDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}
