package com.suyashsrijan.londonmeetup.activities;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.suyashsrijan.londonmeetup.API.tfl.TfLAPI;
import com.suyashsrijan.londonmeetup.API.tfl.interfaces.ApiResultCallback;
import com.suyashsrijan.londonmeetup.API.tfl.models.Bike;
import com.suyashsrijan.londonmeetup.API.tfl.models.Bus;
import com.suyashsrijan.londonmeetup.API.tfl.models.Tube;
import com.suyashsrijan.londonmeetup.R;
import com.suyashsrijan.londonmeetup.enums.Constants;
import com.suyashsrijan.londonmeetup.fragments.BikeInfoBottomSheetDialogFragment;
import com.suyashsrijan.londonmeetup.models.BikeMarkerData;
import com.suyashsrijan.londonmeetup.models.BusMarkerData;
import com.suyashsrijan.londonmeetup.models.Friend;
import com.suyashsrijan.londonmeetup.models.TubeMarkerData;
import com.suyashsrijan.londonmeetup.utils.BitmapUtils;
import com.suyashsrijan.londonmeetup.utils.SmsUtils;
import com.suyashsrijan.londonmeetup.utils.Utils;

import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "London Meetup [Main]";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LatLng mCurrentLocation = null;
    private LocationRequest mLocationRequest;
    private Marker mCurrentLocationMarker = null;
    private Marker mMeetupLocationMarker = null;
    private ArrayList<Marker> mFriendLocations;
    private boolean mHasUpdatedLocationFirstTime = false;
    private TfLAPI mTflApi = new TfLAPI(this);
    private ProgressDialog mLoadingStopsPd = null;
    private MainActivityBroadcastListener mMainActivityBroadcastListener;
    private boolean mShowTubeStops;
    private boolean mShowBusStops;
    private boolean mShowCycleStops;
    private int mRadius;
    private boolean mUseMockLocation;
    private double mMockLocationLat;
    private double mMockLocationLon;
    private FloatingActionButton mFabMainMenu, mFabShowCurrentLocation, mFabSendCurrentLocation, mFabSetMeetLocation;
    private LinearLayout mFabLayout1, mFabLayout2, mFabLayout3;
    private View mFabOverlay;
    private boolean mIsFabMenuOpen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpFABs();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 666);
        }
        mMainActivityBroadcastListener = new MainActivityBroadcastListener();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.INTENT_RELOAD_SETTINGS);
        filter.addAction(Constants.INTENT_UPDATE_MEETUP_LOCATION);
        filter.addAction(Constants.INTENT_ADD_FRIEND_CURRENT_LOCATION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMainActivityBroadcastListener, filter);
        reloadSettings(false);
        initFlow();
    }

    public void reloadSettings(boolean reinitializeApp) {
        mRadius = PreferenceManager.getDefaultSharedPreferences(this).getInt("stopsMaxRadiusPreference", 2) * 100;
        mShowTubeStops = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("showTubeStopsPreference", true);
        mShowBusStops = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("showBusStopsPreference", true);
        mShowCycleStops = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("showCycleStopsPreference", true);
        mUseMockLocation = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("useMockLocationPreference", false);
        mMockLocationLat = PreferenceManager.getDefaultSharedPreferences(this).getFloat("mockLocationLat", 0.0f);
        mMockLocationLon = PreferenceManager.getDefaultSharedPreferences(this).getFloat("mockLocationLon", 0.0f);

        if (reinitializeApp) {
            invalidateAppState();
            initFlow();
        }
    }

    public void initFlow() {
        if (mUseMockLocation) {
            if (mMockLocationLat != 0 && mMockLocationLon != 0) {
                initMockLocationFlow();
            } else {
                initLocationFlow();
            }
        } else {
            initLocationFlow();
        }
    }

    public void initLocationFlow() {
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    public void initMockLocationFlow() {
        mCurrentLocation = new LatLng(mMockLocationLat, mMockLocationLon);
        initGoogleMaps();
    }

    public void initGoogleMaps() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void invalidateAppState() {
        mMap.clear();
        mCurrentLocation = null;
        mCurrentLocationMarker = null;
        mHasUpdatedLocationFirstTime = false;
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.action_show_friends:
                startActivity(new Intent(this, ManageFriendsActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);
        if (mUseMockLocation && mMockLocationLat != 0 && mMockLocationLon != 0) {
            showCurrentLocationOnMap();
            showNearbyTubeBusBikeStations();
            showFriendsAndMeetupLocs();
            mHasUpdatedLocationFirstTime = true;
        } else {
            showCurrentLocationOnMap();
        }
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {

            }

            @Override
            public void onMarkerDragEnd(Marker arg0) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
            }
        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker == mCurrentLocationMarker) {
            Log.i(TAG, "Clicked on current location, ignoring...");
        } else if (marker == mMeetupLocationMarker && !marker.getTitle().equals("Meet-up location")) {
            try {
                SmsUtils.sendMeetupLocation(getApplicationContext(), marker.getPosition());
            } catch (Exception e ) {
                Utils.showErrorDialog(getApplicationContext(), "Error", getString(R.string.fab_send_meetup_loc_error_text) + e.toString());
            }
            marker.hideInfoWindow();
        } else if (marker.getTag() instanceof TubeMarkerData) {
            Intent intent = new Intent(this, TubeDeparturesActivity.class);
            TubeMarkerData mkData = (TubeMarkerData) marker.getTag();
            intent.putExtra("stopId", mkData.getStopId());
            startActivity(intent);
        } else if (marker.getTag() instanceof BusMarkerData) {
            Intent intent = new Intent(this, BusDeparturesActivity.class);
            BusMarkerData mkData = (BusMarkerData) marker.getTag();
            intent.putExtra("stopId", mkData.getStopId());
            startActivity(intent);
        } else if (marker.getTag() instanceof BikeMarkerData) {
            BikeMarkerData mkData = (BikeMarkerData) marker.getTag();
            BottomSheetDialogFragment bikeBottomSheet = BikeInfoBottomSheetDialogFragment.newInstance(mkData.getStopName(), mkData.getBikesAvailable(), mkData.getBikesTaken());
            bikeBottomSheet.show(getSupportFragmentManager(), bikeBottomSheet.getTag());
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 666: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.coordinatorLayoutMain), "Location permission granted!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    mMap.setMyLocationEnabled(true);
                    if (mGoogleApiClient != null) {
                        mGoogleApiClient.reconnect();
                    }
                } else {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.coordinatorLayoutMain), "The location permission was not granted", Snackbar.LENGTH_LONG)
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
        initGoogleMaps();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMainActivityBroadcastListener);
    }

    @Override
    public void onBackPressed() {
        if(mIsFabMenuOpen){
            closeFABMenu();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        if (!mHasUpdatedLocationFirstTime) {
            showCurrentLocationOnMap();
            showNearbyTubeBusBikeStations();
            showFriendsAndMeetupLocs();
            mHasUpdatedLocationFirstTime = true;
        }

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    public void showCurrentLocationOnMap() {
        if (mCurrentLocation != null) {
            if (mCurrentLocationMarker != null) {
                mCurrentLocationMarker.remove();
            }
            mCurrentLocationMarker = mMap.addMarker(new MarkerOptions()
                    .position(mCurrentLocation)
                    .icon(BitmapUtils.getMarkerIconFromDrawable(this, R.drawable.current_location_marker))
                    .title("Your current location"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, 15));
        }
    }

    public void setUpFABs() {
        mFabLayout1 = findViewById(R.id.fabLayout1);
        mFabLayout2 = findViewById(R.id.fabLayout2);
        mFabLayout3 = findViewById(R.id.fabLayout3);
        mFabMainMenu = findViewById(R.id.fab);
        mFabShowCurrentLocation = findViewById(R.id.fab1);
        mFabSendCurrentLocation = findViewById(R.id.fab2);
        mFabSetMeetLocation = findViewById(R.id.fab3);
        mFabOverlay = findViewById(R.id.fabOverlay);

        mFabMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mIsFabMenuOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

        mFabOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
            }
        });

        mFabShowCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCurrentLocationOnMap();
                closeFABMenu();
            }
        });

        mFabSendCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SmsUtils.sendMyLocation(getApplicationContext(), mCurrentLocation);
                } catch (Exception e) {
                    Utils.showErrorDialog(getApplicationContext(), "Error", getString(R.string.fab_send_loc_error_text) + e.toString());
                }
            }
        });

        mFabSetMeetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMeetupLocationMarker != null) {
                    mMeetupLocationMarker.remove();
                }
                LatLng tempMkLocation = new LatLng(mCurrentLocation.latitude + 0.0010, mCurrentLocation.longitude + 0.0010);
                mMeetupLocationMarker = mMap.addMarker(new MarkerOptions()
                        .position(tempMkLocation)
                        .icon(BitmapUtils.getMarkerIconFromDrawable(getApplicationContext(), R.drawable.meetup_marker))
                        .draggable(true)
                        .snippet("Tap to send meet-up location")
                        .title("Meet-up location"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tempMkLocation, 18));
                closeFABMenu();
            }
        });
    }

    private void showFABMenu(){
        mIsFabMenuOpen = true;
        mFabLayout1.setVisibility(View.VISIBLE);
        mFabLayout2.setVisibility(View.VISIBLE);
        mFabLayout3.setVisibility(View.VISIBLE);
        mFabOverlay.setVisibility(View.VISIBLE);
        mFabMainMenu.animate().rotationBy(180).setDuration(300);
        mFabLayout1.animate().translationY(-getResources().getDimension(R.dimen.standard_55)).setDuration(300);
        mFabLayout2.animate().translationY(-getResources().getDimension(R.dimen.standard_100)).setDuration(320);
        mFabLayout3.animate().translationY(-getResources().getDimension(R.dimen.standard_145)).setDuration(340);
    }

    private void closeFABMenu(){
        mIsFabMenuOpen = false;
        mFabOverlay.setVisibility(View.GONE);
        mFabMainMenu.animate().rotationBy(-180);
        mFabLayout1.animate().translationY(0);
        mFabLayout2.animate().translationY(0);
        mFabLayout3.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(!mIsFabMenuOpen){
                    mFabLayout1.setVisibility(View.GONE);
                    mFabLayout2.setVisibility(View.GONE);
                    mFabLayout3.setVisibility(View.GONE);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    public void showNearbyTubeBusBikeStations() {
        final Stack<Boolean> loadingStops = new Stack<>();
        if (mShowTubeStops) { loadingStops.push(true); }
        if (mShowBusStops) { loadingStops.push(true); }
        if (mShowCycleStops) { loadingStops.push(true); }

        if (!loadingStops.isEmpty()) {
            mLoadingStopsPd = new ProgressDialog(MainActivity.this);
            mLoadingStopsPd.setMessage("Loading nearby stops...");
            mLoadingStopsPd.setIndeterminate(true);
            mLoadingStopsPd.setCancelable(false);
            mLoadingStopsPd.show();
        }

        if (mShowTubeStops) {
            mTflApi.getNearbyTubes(mRadius, mCurrentLocation, new ApiResultCallback<Tube>() {
                @Override
                public void response(Context context, ArrayList<Tube> output) {
                    loadingStops.pop();
                    for (Tube tubeStop : output) {
                        mMap.addMarker(new MarkerOptions()
                                .position(tubeStop.getCoordinates())
                                .icon(BitmapUtils.getMarkerIconFromDrawable(context, R.drawable.tube_marker))
                                .title(tubeStop.getStopName())
                                .snippet("Tap for tube departures"))
                                .setTag(new TubeMarkerData(tubeStop.getStopId()));
                    }
                    if (loadingStops.isEmpty()) {
                        if (mLoadingStopsPd != null) {
                            mLoadingStopsPd.dismiss();
                        }
                    }
                }

                @Override
                public void error(Context context, Exception exception) {
                    loadingStops.pop();
                    Log.e(TAG, exception.toString());
                    if (loadingStops.isEmpty()) {
                        if (mLoadingStopsPd != null) {
                            mLoadingStopsPd.dismiss();
                        }
                    }
                    Utils.showErrorDialog(context, "Error", "There was an error while loading tube stations: " + exception.getMessage());
                }
            });
        }

        if (mShowBusStops) {
            mTflApi.getNearbyBusStops(mRadius, mCurrentLocation, new ApiResultCallback<Bus>() {
                @Override
                public void response(Context context, ArrayList<Bus> output) {
                    loadingStops.pop();
                    for (Bus busStop : output) {
                        mMap.addMarker(new MarkerOptions()
                                .position(busStop.getCoordinates())
                                .icon(BitmapUtils.getMarkerIconFromDrawable(context, R.drawable.bus_marker))
                                .title(busStop.getStopName() + " " + "(" + busStop.getStopIndicator() + ")")
                                .snippet("Tap for bus departures"))
                                .setTag(new BusMarkerData(busStop.getStopId()));
                    }
                    if (loadingStops.isEmpty()) {
                        if (mLoadingStopsPd != null) {
                            mLoadingStopsPd.dismiss();
                        }
                    }

                }
                @Override
                public void error(Context context, Exception exception) {
                    loadingStops.pop();
                    Log.e(TAG, exception.toString());
                    if (loadingStops.isEmpty()) {
                        if (mLoadingStopsPd != null) {
                            mLoadingStopsPd.dismiss();
                        }
                    }
                    Utils.showErrorDialog(context, "Error", "There was an error while loading bus stops/stations: " + exception.getMessage());
                }
            });
        }

        if (mShowCycleStops) {
            mTflApi.getNearbyBikePoints(mRadius, mCurrentLocation, new ApiResultCallback<Bike>() {
                @Override
                public void response(Context context, ArrayList<Bike> output) {
                    loadingStops.pop();
                    for (Bike bikePoint : output) {
                        mMap.addMarker(new MarkerOptions()
                                .position(bikePoint.getCoordinates())
                                .icon(BitmapUtils.getMarkerIconFromDrawable(context, R.drawable.cycle_marker))
                                .title(bikePoint.getStopName())
                                .snippet("Tap for bike info"))
                                .setTag(new BikeMarkerData(bikePoint.getStopName(), bikePoint.getBikesAvailable(), bikePoint.getBikesTaken(), bikePoint.getTotalBikes()));
                    }
                    if (loadingStops.isEmpty()) {
                        if (mLoadingStopsPd != null) {
                            mLoadingStopsPd.dismiss();
                        }
                    }
                }
                @Override
                public void error(Context context, Exception exception) {
                    loadingStops.pop();
                    Log.e(TAG, exception.toString());
                    if (loadingStops.isEmpty()) {
                        if (mLoadingStopsPd != null) {
                            mLoadingStopsPd.dismiss();
                        }
                    }
                    Utils.showErrorDialog(context, "Error", "There was an error while loading bike docking stations: " + exception.getMessage());
                }
            });
        }

    }

    public void showFriendsAndMeetupLocs() {
        if (mMeetupLocationMarker != null) {
            LatLng meetupLoc = mMeetupLocationMarker.getPosition();
            mMeetupLocationMarker.remove();
            mMeetupLocationMarker = mMap.addMarker(new MarkerOptions()
                    .position(meetupLoc)
                    .icon(BitmapUtils.getMarkerIconFromDrawable(this, R.drawable.meetup_marker))
                    .title("Meet-up location"));
        }
        if (mFriendLocations != null && mFriendLocations.size() != 0) {
            for (Marker m : mFriendLocations) {
                Friend friendData = (Friend) m.getTag();
                Marker friendMarker = mMap.addMarker(new MarkerOptions()
                        .position(m.getPosition())
                        .icon(BitmapUtils.getMarkerIconFromDrawable(this, R.drawable.friend_marker))
                        .title(friendData.getFriendName()));
                friendMarker.setTag(friendData);
            }
        }
    }

    class MainActivityBroadcastListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.INTENT_RELOAD_SETTINGS)) {
                reloadSettings(intent.getBooleanExtra("reinitApp", false));
            } else if (intent.getAction().equals(Constants.INTENT_UPDATE_MEETUP_LOCATION)) {
                double lat, lon;
                LatLng coordinates;
                lat = intent.getDoubleExtra("lat", 0);
                lon = intent.getDoubleExtra("lon", 0);
                coordinates = new LatLng(lat, lon);
                if (lat != 0 && lon != 0) {
                    if (mMeetupLocationMarker != null) {
                        mMeetupLocationMarker.remove();
                    }
                    mMeetupLocationMarker = mMap.addMarker(new MarkerOptions()
                            .position(coordinates)
                            .icon(BitmapUtils.getMarkerIconFromDrawable(context, R.drawable.meetup_marker))
                            .title("Meet-up location"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15));
                }
            } else if (intent.getAction().equals(Constants.INTENT_ADD_FRIEND_CURRENT_LOCATION)) {
                double lat, lon;
                String friendName, friendNumber;
                LatLng coordinates;
                lat = intent.getDoubleExtra("lat", 0);
                lon = intent.getDoubleExtra("lon", 0);
                friendName = intent.getStringExtra("name");
                friendNumber = intent.getStringExtra("number");
                coordinates = new LatLng(lat, lon);

                if (mFriendLocations == null) {
                    mFriendLocations = new ArrayList<>();
                }

                if (mFriendLocations.isEmpty()) {
                    Marker friendMarker = mMap.addMarker(new MarkerOptions()
                            .position(coordinates)
                            .icon(BitmapUtils.getMarkerIconFromDrawable(context, R.drawable.friend_marker))
                            .title(friendName));
                    friendMarker.setTag(new Friend(friendName, friendNumber));
                    mFriendLocations.add(friendMarker);
                } else {
                    boolean found = false;
                    for (int i = 0; i < mFriendLocations.size(); i++) {
                        Friend friendData = (Friend) mFriendLocations.get(i).getTag();
                        if (friendData.getFriendName().equals(friendName) && friendData.getFriendMobileNumber().equals(friendNumber)) {
                            mFriendLocations.get(i).setPosition(new LatLng(lat, lon));
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        Marker friendMarker = mMap.addMarker(new MarkerOptions()
                                .position(coordinates)
                                .icon(BitmapUtils.getMarkerIconFromDrawable(context, R.drawable.friend_marker))
                                .title(friendName));
                        friendMarker.setTag(new Friend(friendName, friendNumber));
                        mFriendLocations.add(friendMarker);
                    }
                }
            }
        }
    }
}