package com.suyashsrijan.londonmeetup.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.suyashsrijan.londonmeetup.R;
import com.suyashsrijan.londonmeetup.adapters.RVFriendsAdapter;
import com.suyashsrijan.londonmeetup.models.Friend;
import com.suyashsrijan.londonmeetup.utils.IOUtils;
import com.suyashsrijan.londonmeetup.utils.SmsUtils;
import com.suyashsrijan.londonmeetup.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ManageFriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_friends);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_PHONE_STATE}, 667);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final RecyclerView rv = findViewById(R.id.recyclerViewFriends);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);

        try {
            ArrayList<Friend> friendArrayList = new ArrayList<>();
            String friendsListData = IOUtils.readFileString(getApplicationContext(), "friends_list.json");
            JSONArray friendsList = new JSONArray(friendsListData);
            for (int i = 0; i < friendsList.length(); i++) {
                JSONObject friend = friendsList.getJSONObject(i);
                friendArrayList.add(new Friend(friend.getString("friend_name"), friend.getString("friend_number")));
            }
            rv.setAdapter(new RVFriendsAdapter(friendArrayList));
            rv.startLayoutAnimation();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 666: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), "SMS permissions granted!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), "The SMS permission(s) were not granted", Snackbar.LENGTH_LONG)
                            .setAction("OPEN SETTINGS", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Utils.openAppSettings(getApplicationContext());
                                }
                            });
                    snackbar.show();
                }
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manage_friends_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_add_friend:
                showAddFriendsDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showAddFriendsDialog() {
        if (!PreferenceManager.getDefaultSharedPreferences(this).contains("displayName") && !PreferenceManager.getDefaultSharedPreferences(this).contains("mobileNumber")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle("Error");
            builder.setMessage(R.string.friends_setup_name_and_mobile_error_text);
            builder.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    startActivity(new Intent(ManageFriendsActivity.this, SettingsActivity.class));
                }
            });
            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(ManageFriendsActivity.this, SettingsActivity.class));
                }
            });
            builder.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle("Input friend mobile number");
            LinearLayout layout = new LinearLayout(getApplicationContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(50,25,50, 0);

            final EditText friendNumberEditText = new EditText(this);
            friendNumberEditText.setHint("Mobile number...");
            layout.addView(friendNumberEditText);

            builder.setView(layout);
            builder.setPositiveButton("Send request", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (Utils.isValidUKMobileNumber(friendNumberEditText.getText().toString())) {
                        try {
                            SmsUtils.sendFriendRequest(getApplicationContext(), friendNumberEditText.getText().toString());
                        } catch (Exception e) {
                            Utils.showErrorDialog(ManageFriendsActivity.this, "Error", getString(R.string.friends_unable_to_send_friend_request_error_text) + e.getMessage());
                        }
                        dialogInterface.dismiss();
                    } else {
                        Utils.showErrorDialog(ManageFriendsActivity.this, "Error", getString(R.string.friends_invalid_name_or_mobile_error_text));
                    }
                }
            });
            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }
    }
}