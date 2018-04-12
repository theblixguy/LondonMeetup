package com.suyashsrijan.londonmeetup.activities

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceManager
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.Places
import com.google.android.gms.location.places.ui.PlacePicker
import com.suyashsrijan.londonmeetup.R
import com.suyashsrijan.londonmeetup.enums.Constants
import com.suyashsrijan.londonmeetup.interfaces.OnPreferenceChange
import com.suyashsrijan.londonmeetup.utils.Utils

class SettingsActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnPreferenceChange {
    private var mGoogleApiClient: GoogleApiClient? = null
    private var prefsChanged = false
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .build()

        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingsFragment())
                .commit()

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

    }

    override fun onPreferenceChange() {
        setPrefsChanged()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlacePicker.getPlace(this, data)
                val preferencesEditor = sharedPreferences!!.edit()
                preferencesEditor.putFloat("mockLocationLat", place.latLng.latitude.toFloat())
                preferencesEditor.putFloat("mockLocationLon", place.latLng.longitude.toFloat())
                preferencesEditor.apply()
                setPrefsChanged()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(Constants.INTENT_RELOAD_SETTINGS)
        intent.putExtra("reinitApp", prefsChanged)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    override fun onConnected(bundle: Bundle?) {

    }

    override fun onConnectionSuspended(i: Int) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    public override fun onStart() {
        super.onStart()
        mGoogleApiClient!!.connect()
    }

    public override fun onStop() {
        super.onStop()
        mGoogleApiClient!!.disconnect()
    }

    fun setPrefsChanged() {
        prefsChanged = true
    }

    class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

        internal lateinit var mCallback: OnPreferenceChange

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.pref_main)
            val useMockLocation = findPreference("useMockLocationPreference")
            val showPlacePicker = findPreference("showPlacePickerPreference")
            val showCoordinatesInput = findPreference("showCoordinatesInputPreference")
            val yourNameInput = findPreference("yourNamePreference")
            val yourMobileNumberInput = findPreference("yourMobileNumberPreference")
            if (!PreferenceManager.getDefaultSharedPreferences(activity!!).getBoolean("useMockLocationPreference", false)) {
                showPlacePicker.isEnabled = false
                showCoordinatesInput.isEnabled = false
            }
            useMockLocation.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, o ->
                val newValue = o as Boolean
                if (newValue) {
                    showPlacePicker.isEnabled = true
                    showCoordinatesInput.isEnabled = true
                } else {
                    showPlacePicker.isEnabled = false
                    showCoordinatesInput.isEnabled = false
                }
                true
            }
            showPlacePicker.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val builder = PlacePicker.IntentBuilder()
                try {
                    activity!!.startActivityForResult(builder.build(activity!!), PLACE_PICKER_REQUEST_CODE)
                } catch (e: GooglePlayServicesRepairableException) {
                    e.printStackTrace()
                } catch (e: GooglePlayServicesNotAvailableException) {
                    e.printStackTrace()
                }

                true
            }
            showCoordinatesInput.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val builder = AlertDialog.Builder(activity!!, R.style.AppCompatAlertDialogStyle)
                builder.setTitle("Input coordinates (lat/long)")
                val layout = LinearLayout(activity)
                layout.orientation = LinearLayout.VERTICAL
                layout.setPadding(50, 25, 50, 0)

                val latEditText = EditText(activity)
                latEditText.hint = "Latitude..."
                latEditText.setText(java.lang.Float.toString(PreferenceManager.getDefaultSharedPreferences(activity!!).getFloat("mockLocationLat", 0.0f)))
                layout.addView(latEditText)

                val lonEditText = EditText(activity)
                lonEditText.hint = "Longitude..."
                lonEditText.setText(java.lang.Float.toString(PreferenceManager.getDefaultSharedPreferences(activity!!).getFloat("mockLocationLon", 0.0f)))
                layout.addView(lonEditText)

                builder.setView(layout)
                builder.setPositiveButton("Save") { dialogInterface, i ->
                    if (Utils.isValidCoordinates(latEditText.text.toString(), lonEditText.text.toString())) {
                        val preferencesEditor = PreferenceManager.getDefaultSharedPreferences(activity!!).edit()
                        preferencesEditor.putFloat("mockLocationLat", java.lang.Float.parseFloat(latEditText.text.toString()))
                        preferencesEditor.putFloat("mockLocationLon", java.lang.Float.parseFloat(lonEditText.text.toString()))
                        preferencesEditor.apply()
                        (activity as SettingsActivity).setPrefsChanged()
                        dialogInterface.dismiss()
                    } else {
                        Utils.showErrorDialog(activity!!, "Error", getString(R.string.settings_invalid_coords_error_text))
                    }
                }
                builder.setNegativeButton("Close") { dialogInterface, i -> dialogInterface.dismiss() }
                builder.show()

                true
            }

            yourNameInput.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val builder = AlertDialog.Builder(activity!!, R.style.AppCompatAlertDialogStyle)
                builder.setTitle("Input display name")
                val layout = LinearLayout(activity)
                layout.orientation = LinearLayout.VERTICAL
                layout.setPadding(50, 25, 50, 0)

                val displayNameText = EditText(activity)
                displayNameText.hint = "Your name..."
                displayNameText.setText(PreferenceManager.getDefaultSharedPreferences(activity!!).getString("displayName", null))
                layout.addView(displayNameText)
                builder.setView(layout)
                builder.setPositiveButton("Save") { dialogInterface, i ->
                    if (Utils.isValidName(displayNameText.text.toString())) {
                        val preferencesEditor = PreferenceManager.getDefaultSharedPreferences(activity!!).edit()
                        preferencesEditor.putString("displayName", displayNameText.text.toString())
                        preferencesEditor.apply()
                        dialogInterface.dismiss()
                    } else {
                        Utils.showErrorDialog(activity!!, "Error", getString(R.string.settings_invalid_name_error_text))
                    }
                }
                builder.setNegativeButton("Close") { dialogInterface, i -> dialogInterface.dismiss() }
                builder.show()
                true
            }

            yourMobileNumberInput.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val builder = AlertDialog.Builder(activity!!, R.style.AppCompatAlertDialogStyle)
                builder.setTitle("Input mobile number")
                val layout = LinearLayout(activity)
                layout.orientation = LinearLayout.VERTICAL
                layout.setPadding(50, 25, 50, 0)

                val mobileNumberText = EditText(activity)
                mobileNumberText.hint = "Your mobile number..."
                mobileNumberText.setText(PreferenceManager.getDefaultSharedPreferences(activity!!).getString("mobileNumber", null))
                layout.addView(mobileNumberText)
                builder.setView(layout)
                builder.setPositiveButton("Save") { dialogInterface, i ->
                    if (Utils.isValidUKMobileNumber(mobileNumberText.text.toString())) {
                        val preferencesEditor = PreferenceManager.getDefaultSharedPreferences(activity!!).edit()
                        preferencesEditor.putString("mobileNumber", mobileNumberText.text.toString())
                        preferencesEditor.apply()
                        dialogInterface.dismiss()
                    } else {
                        Utils.showErrorDialog(activity!!, "Error", getString(R.string.settings_invalid_mobile_number_error_text))
                    }
                }
                builder.setNegativeButton("Close") { dialogInterface, i -> dialogInterface.dismiss() }
                builder.show()
                true
            }
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
            mCallback.onPreferenceChange()
        }

        override fun onAttach(context: Context?) {
            super.onAttach(context)
            mCallback = activity as OnPreferenceChange
        }
    }

    companion object {
        private val PLACE_PICKER_REQUEST_CODE = 1234
    }


}
