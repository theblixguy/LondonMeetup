package com.suyashsrijan.londonmeetup.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.suyashsrijan.londonmeetup.R;
import com.suyashsrijan.londonmeetup.enums.Constants;
import com.suyashsrijan.londonmeetup.interfaces.OnPreferenceChange;
import com.suyashsrijan.londonmeetup.utils.Utils;

public class SettingsActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnPreferenceChange {

    private static final int PLACE_PICKER_REQUEST_CODE = 1234;
    private GoogleApiClient mGoogleApiClient;
    private boolean prefsChanged = false;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .build();

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    @Override
    public void onPreferenceChange() {
        setPrefsChanged();
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
                preferencesEditor.putFloat("mockLocationLat", (float) place.getLatLng().latitude);
                preferencesEditor.putFloat("mockLocationLon", (float) place.getLatLng().longitude);
                preferencesEditor.apply();
                setPrefsChanged();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(Constants.INTENT_RELOAD_SETTINGS);
        intent.putExtra("reinitApp", prefsChanged);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    public void setPrefsChanged() {
        prefsChanged = true;
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

        OnPreferenceChange mCallback;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.pref_main);
            Preference useMockLocation = findPreference("useMockLocationPreference");
            final Preference showPlacePicker = findPreference("showPlacePickerPreference");
            final Preference showCoordinatesInput = findPreference("showCoordinatesInputPreference");
            final Preference yourNameInput = findPreference("yourNamePreference");
            final Preference yourMobileNumberInput = findPreference("yourMobileNumberPreference");
            if (!PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("useMockLocationPreference", false)) {
                showPlacePicker.setEnabled(false);
                showCoordinatesInput.setEnabled(false);
            }
            useMockLocation.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    boolean newValue = (boolean) o;
                    if (newValue) {
                        showPlacePicker.setEnabled(true);
                        showCoordinatesInput.setEnabled(true);
                    } else {
                        showPlacePicker.setEnabled(false);
                        showCoordinatesInput.setEnabled(false);
                    }
                    return true;
                }
            });
            showPlacePicker.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                        try {
                            getActivity().startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST_CODE);
                        } catch (GooglePlayServicesRepairableException e) {
                            e.printStackTrace();
                        } catch (GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        }
                    return true;
                }
            });
            showCoordinatesInput.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Input coordinates (lat/long)");
                    LinearLayout layout = new LinearLayout(getActivity());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setPadding(50,25,50, 0);

                    final EditText latEditText = new EditText(getActivity());
                    latEditText.setHint("Latitude...");
                    latEditText.setText(Float.toString(PreferenceManager.getDefaultSharedPreferences(getActivity()).getFloat("mockLocationLat", 0.0f)));
                    layout.addView(latEditText);

                    final EditText lonEditText = new EditText(getActivity());
                    lonEditText.setHint("Longitude...");
                    lonEditText.setText(Float.toString(PreferenceManager.getDefaultSharedPreferences(getActivity()).getFloat("mockLocationLon", 0.0f)));
                    layout.addView(lonEditText);

                    builder.setView(layout);
                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                                if (Utils.isValidCoordinates(latEditText.getText().toString(),  lonEditText.getText().toString())) {
                                    SharedPreferences.Editor preferencesEditor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                                    preferencesEditor.putFloat("mockLocationLat", Float.parseFloat(latEditText.getText().toString()));
                                    preferencesEditor.putFloat("mockLocationLon", Float.parseFloat(lonEditText.getText().toString()));
                                    preferencesEditor.apply();
                                    ((SettingsActivity)getActivity()).setPrefsChanged();
                                    dialogInterface.dismiss();
                                } else {
                                    Utils.showErrorDialog(getActivity(), "Error", getString(R.string.settings_invalid_coords_error_text));
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

                    return true;
                }
            });

            yourNameInput.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Input display name");
                    LinearLayout layout = new LinearLayout(getActivity());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setPadding(50,25,50, 0);

                    final EditText displayNameText = new EditText(getActivity());
                    displayNameText.setHint("Your name...");
                    displayNameText.setText(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("displayName", null));
                    layout.addView(displayNameText);
                    builder.setView(layout);
                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (Utils.isValidName(displayNameText.getText().toString())) {
                                SharedPreferences.Editor preferencesEditor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                                preferencesEditor.putString("displayName", displayNameText.getText().toString());
                                preferencesEditor.apply();
                                dialogInterface.dismiss();
                            } else {
                                Utils.showErrorDialog(getActivity(), "Error", getString(R.string.settings_invalid_name_error_text));
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
                    return true;
                }
            });

            yourMobileNumberInput.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Input mobile number");
                    LinearLayout layout = new LinearLayout(getActivity());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setPadding(50,25,50, 0);

                    final EditText mobileNumberText = new EditText(getActivity());
                    mobileNumberText.setHint("Your mobile number...");
                    mobileNumberText.setText(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("mobileNumber", null));
                    layout.addView(mobileNumberText);
                    builder.setView(layout);
                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (Utils.isValidUKMobileNumber(mobileNumberText.getText().toString())) {
                                SharedPreferences.Editor preferencesEditor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                                preferencesEditor.putString("mobileNumber", mobileNumberText.getText().toString());
                                preferencesEditor.apply();
                                dialogInterface.dismiss();
                            } else {
                                Utils.showErrorDialog(getActivity(), "Error", getString(R.string.settings_invalid_mobile_number_error_text));
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
                    return true;
                }
            });
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            mCallback.onPreferenceChange();
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            mCallback = (OnPreferenceChange) getActivity();
        }
    }


}
