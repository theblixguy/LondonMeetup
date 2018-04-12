package com.suyashsrijan.londonmeetup.activities

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout

import com.suyashsrijan.londonmeetup.R
import com.suyashsrijan.londonmeetup.adapters.RVFriendsAdapter
import com.suyashsrijan.londonmeetup.models.Friend
import com.suyashsrijan.londonmeetup.utils.IOUtils
import com.suyashsrijan.londonmeetup.utils.SmsUtils
import com.suyashsrijan.londonmeetup.utils.Utils

import org.json.JSONArray
import org.json.JSONObject

import java.util.ArrayList

class ManageFriendsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_friends)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_PHONE_STATE), 667)
        }

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        val rv = findViewById<RecyclerView>(R.id.recyclerViewFriends)
        val llm = LinearLayoutManager(this)
        rv.setHasFixedSize(true)
        rv.layoutManager = llm

        try {
            val friendArrayList = ArrayList<Friend>()
            val friendsListData = IOUtils.readFileString(applicationContext, "friends_list.json")
            val friendsList = JSONArray(friendsListData)
            for (i in 0 until friendsList.length()) {
                val friend = friendsList.getJSONObject(i)
                friendArrayList.add(Friend(friend.getString("friend_name"), friend.getString("friend_number")))
            }
            rv.adapter = RVFriendsAdapter(friendArrayList)
            rv.startLayoutAnimation()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            666 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    val snackbar = Snackbar
                            .make(findViewById(android.R.id.content), "SMS permissions granted!", Snackbar.LENGTH_SHORT)
                    snackbar.show()
                } else {
                    val snackbar = Snackbar
                            .make(findViewById(android.R.id.content), "The SMS permission(s) were not granted", Snackbar.LENGTH_LONG)
                            .setAction("OPEN SETTINGS") { Utils.openAppSettings(applicationContext) }
                    snackbar.show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.manage_friends_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.action_add_friend -> showAddFriendsDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    fun showAddFriendsDialog() {
        if (!PreferenceManager.getDefaultSharedPreferences(this).contains("displayName") && !PreferenceManager.getDefaultSharedPreferences(this).contains("mobileNumber")) {
            val builder = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
            builder.setTitle("Error")
            builder.setMessage(R.string.friends_setup_name_and_mobile_error_text)
            builder.setPositiveButton("Open Settings") { dialogInterface, i ->
                dialogInterface.dismiss()
                startActivity(Intent(this@ManageFriendsActivity, SettingsActivity::class.java))
            }
            builder.setNegativeButton("Close") { dialogInterface, i -> startActivity(Intent(this@ManageFriendsActivity, SettingsActivity::class.java)) }
            builder.show()
        } else {
            val builder = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
            builder.setTitle("Input friend mobile number")
            val layout = LinearLayout(applicationContext)
            layout.orientation = LinearLayout.VERTICAL
            layout.setPadding(50, 25, 50, 0)

            val friendNumberEditText = EditText(this)
            friendNumberEditText.hint = "Mobile number..."
            layout.addView(friendNumberEditText)

            builder.setView(layout)
            builder.setPositiveButton("Send request") { dialogInterface, i ->
                if (Utils.isValidUKMobileNumber(friendNumberEditText.text.toString())) {
                    try {
                        SmsUtils.sendFriendRequest(applicationContext, friendNumberEditText.text.toString())
                    } catch (e: Exception) {
                        Utils.showErrorDialog(this@ManageFriendsActivity, "Error", getString(R.string.friends_unable_to_send_friend_request_error_text) + e.message)
                    }

                    dialogInterface.dismiss()
                } else {
                    Utils.showErrorDialog(this@ManageFriendsActivity, "Error", getString(R.string.friends_invalid_name_or_mobile_error_text))
                }
            }
            builder.setNegativeButton("Close") { dialogInterface, i -> dialogInterface.dismiss() }
            builder.show()
        }
    }
}