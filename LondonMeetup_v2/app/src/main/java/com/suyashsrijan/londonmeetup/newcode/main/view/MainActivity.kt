package com.suyashsrijan.londonmeetup.newcode.main.view

import android.Manifest
import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.patloew.rxlocation.RxLocation
import com.suyashsrijan.londonmeetup.R
import com.suyashsrijan.londonmeetup.activities.ManageFriendsActivity
import com.suyashsrijan.londonmeetup.activities.SettingsActivity
import com.suyashsrijan.londonmeetup.enums.Constants
import com.suyashsrijan.londonmeetup.fragments.BikeInfoBottomSheetDialogFragment
import com.suyashsrijan.londonmeetup.models.BikeMarkerData
import com.suyashsrijan.londonmeetup.models.BusMarkerData
import com.suyashsrijan.londonmeetup.models.Friend
import com.suyashsrijan.londonmeetup.models.TubeMarkerData
import com.suyashsrijan.londonmeetup.newcode.app.model.Bike
import com.suyashsrijan.londonmeetup.newcode.app.model.Bus
import com.suyashsrijan.londonmeetup.newcode.app.model.Tube
import com.suyashsrijan.londonmeetup.newcode.main.di.MainComponent
import com.suyashsrijan.londonmeetup.newcode.main.presenter.MainPresenter
import com.suyashsrijan.londonmeetup.newcode.main.presenter.MainPresenterImpl
import com.suyashsrijan.londonmeetup.utils.BitmapUtils
import com.suyashsrijan.londonmeetup.utils.SmsUtils
import com.suyashsrijan.londonmeetup.utils.Utils
import java.util.*

class MainActivity : AppCompatActivity(), MainView, OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    private var mCurrentLocation: LatLng? = null
    private var mLocationRequest: LocationRequest? = null
    private var mCurrentLocationMarker: Marker? = null
    private var mMeetupLocationMarker: Marker? = null
    private var mFriendLocations: ArrayList<Marker>? = null
    private var mHasUpdatedLocationFirstTime = false
    private var mMainActivityBroadcastListener: MainActivityBroadcastListener? = null
    private var mShowTubeStops: Boolean = false
    private var mShowBusStops: Boolean = false
    private var mShowCycleStops: Boolean = false
    private var mRadius: Int = 0
    private var mUseMockLocation: Boolean = false
    private var mMockLocationLat: Double = 0.toDouble()
    private var mMockLocationLon: Double = 0.toDouble()
    private var mFabMainMenu: FloatingActionButton? = null
    private var mFabShowCurrentLocation: FloatingActionButton? = null
    private var mFabSendCurrentLocation: FloatingActionButton? = null
    private var mFabSetMeetLocation: FloatingActionButton? = null
    private var mFabLayout1: LinearLayout? = null
    private var mFabLayout2: LinearLayout? = null
    private var mFabLayout3: LinearLayout? = null
    private var mFabOverlay: View? = null
    private var mIsFabMenuOpen = false

    private lateinit var mRxLocation: RxLocation
    private lateinit var presenter: MainPresenter<MainView>
    private lateinit var mapFragment: SupportMapFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupPresenter()
        setUpFABs()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 666)
        }

        mMainActivityBroadcastListener = MainActivityBroadcastListener()
        mRxLocation = RxLocation(this)

        val filter = IntentFilter()
        filter.addAction(Constants.INTENT_RELOAD_SETTINGS)
        filter.addAction(Constants.INTENT_UPDATE_MEETUP_LOCATION)
        filter.addAction(Constants.INTENT_ADD_FRIEND_CURRENT_LOCATION)

        LocalBroadcastManager.getInstance(this).registerReceiver(mMainActivityBroadcastListener!!, filter)
        reloadSettings(false)
        initFlow()
    }

    override fun injectDependencies(mainComponent: MainComponent) {
        mainComponent.inject(this)
    }

    private fun setupPresenter() {
        presenter = MainPresenterImpl()
        presenter.attachView(this)
    }

    override fun displayNearbyTubes(tubesList: List<Tube>) {
        tubesList.forEach { item ->
            mMap!!.addMarker(MarkerOptions()
                    .position(item.coordinates!!)
                    .icon(BitmapUtils.getMarkerIconFromDrawable(this, R.drawable.tube_marker))
                    .title(item.name)
                    .snippet("Tap for tube departures")).tag = TubeMarkerData(item.stopId!!)
        }
    }

    override fun displayNearbyBuses(busesList: List<Bus>) {
        busesList.forEach { busStop ->
            mMap!!.addMarker(MarkerOptions()
                    .position(busStop.coordinates!!)
                    .icon(BitmapUtils.getMarkerIconFromDrawable(this, R.drawable.bus_marker))
                    .title("${busStop.name} (${busStop.indicator})")
                    .snippet("Tap for bus departures")).tag = BusMarkerData(busStop.stopId!!)
        }
    }

    override fun displayNearbyBikes(bikesList: List<Bike>) {
        bikesList.forEach { bikeDockingStation ->
            mMap!!.addMarker(MarkerOptions()
                    .position(bikeDockingStation.coordinates!!)
                    .icon(BitmapUtils.getMarkerIconFromDrawable(this, R.drawable.cycle_marker))
                    .title(bikeDockingStation.name)
                    .snippet("Tap for bike info")).tag = BikeMarkerData(bikeDockingStation.name!!, bikeDockingStation.bikesAvailable!!, bikeDockingStation.bikesTaken!!)
        }
    }

    override fun displayBikeBottomSheet(bikeStationName: String, bikesAvailable: Int, bikesTaken: Int) {
        val bikeBottomSheet = BikeInfoBottomSheetDialogFragment.newInstance(bikeStationName, bikesAvailable, bikesTaken)
        bikeBottomSheet.show(supportFragmentManager, bikeBottomSheet.tag)
    }

    override fun getActivity(): Activity {
        return this
    }

    fun reloadSettings(reinitializeApp: Boolean) {
        mRadius = PreferenceManager.getDefaultSharedPreferences(this).getInt("stopsMaxRadiusPreference", 2) * 100
        mShowTubeStops = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("showTubeStopsPreference", true)
        mShowBusStops = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("showBusStopsPreference", true)
        mShowCycleStops = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("showCycleStopsPreference", true)
        mUseMockLocation = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("useMockLocationPreference", false)
        mMockLocationLat = PreferenceManager.getDefaultSharedPreferences(this).getFloat("mockLocationLat", 0.0f).toDouble()
        mMockLocationLon = PreferenceManager.getDefaultSharedPreferences(this).getFloat("mockLocationLon", 0.0f).toDouble()

        if (reinitializeApp) {
            invalidateAppState()
            initFlow()
        }
    }

    private fun initFlow() {
        if (mUseMockLocation) {
            if (mMockLocationLat != 0.0 && mMockLocationLon != 0.0) {
                initMockLocationFlow()
            } else {
                initLocationFlow()
            }
        } else {
            initLocationFlow()
        }
    }

    private fun initLocationFlow() {
        createLocationRequest()
    }

    private fun initMockLocationFlow() {
        mCurrentLocation = LatLng(mMockLocationLat, mMockLocationLon)
        setupGoogleMaps()
    }

    private fun setupGoogleMaps() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun invalidateAppState() {
        mMap!!.clear()
        mCurrentLocation = null
        mCurrentLocationMarker = null
        mHasUpdatedLocationFirstTime = false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_show_settings -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.action_show_friends -> startActivity(Intent(this, ManageFriendsActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.setOnInfoWindowClickListener {
            presenter.handleInfoWindowClick(it)
        }

        if (mUseMockLocation && mMockLocationLat != 0.0 && mMockLocationLon != 0.0) {
            showCurrentLocationOnMap()
            showFriendsAndMeetupLocs()
            mHasUpdatedLocationFirstTime = true
        } else {
            showCurrentLocationOnMap()
        }

        mMap!!.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(arg0: Marker) {}
            override fun onMarkerDrag(arg0: Marker) {}
            override fun onMarkerDragEnd(arg0: Marker) {
                mMap!!.animateCamera(CameraUpdateFactory.newLatLng(arg0.position))
            }
        })
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            666 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val snackbar = Snackbar
                            .make(findViewById(R.id.coordinatorLayoutMain), "Location permission granted!", Snackbar.LENGTH_SHORT)
                    snackbar.show()
                    mMap!!.isMyLocationEnabled = true
                } else {
                    val snackbar = Snackbar
                            .make(findViewById(R.id.coordinatorLayoutMain), "The location permission was not granted", Snackbar.LENGTH_LONG)
                            .setAction("OPEN SETTINGS") { Utils.openAppSettings(applicationContext) }
                    snackbar.show()
                }
            }
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMainActivityBroadcastListener!!)
    }

    override fun onBackPressed() {
        if (mIsFabMenuOpen) {
            closeFABMenu()
        } else {
            super.onBackPressed()
        }
    }

    protected fun createLocationRequest() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(FASTEST_INTERVAL)
                .setInterval(INTERVAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mRxLocation.location().updates(mLocationRequest!!).take(1).subscribe { location ->
                mCurrentLocation = LatLng(location.latitude, location.longitude)
                setupGoogleMaps()
            }
        }
    }

    fun showCurrentLocationOnMap() {
        if (mCurrentLocation != null) {
            if (mCurrentLocationMarker != null) {
                mCurrentLocationMarker!!.remove()
            }
            mCurrentLocationMarker = mMap!!.addMarker(MarkerOptions()
                    .position(mCurrentLocation!!)
                    .icon(BitmapUtils.getMarkerIconFromDrawable(this, R.drawable.current_location_marker))
                    .title("Your current location"))
            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, 15f))
        }
    }

    private fun setUpFABs() {
        mFabLayout1 = findViewById(R.id.fabLayout1)
        mFabLayout2 = findViewById(R.id.fabLayout2)
        mFabLayout3 = findViewById(R.id.fabLayout3)
        mFabMainMenu = findViewById(R.id.fab)
        mFabShowCurrentLocation = findViewById(R.id.fab1)
        mFabSendCurrentLocation = findViewById(R.id.fab2)
        mFabSetMeetLocation = findViewById(R.id.fab3)
        mFabOverlay = findViewById(R.id.fabOverlay)

        mFabMainMenu!!.setOnClickListener {
            if (!mIsFabMenuOpen) {
                showFABMenu()
            } else {
                closeFABMenu()
            }
        }

        mFabOverlay!!.setOnClickListener { closeFABMenu() }

        mFabShowCurrentLocation!!.setOnClickListener {
            showCurrentLocationOnMap()
            closeFABMenu()
        }

        mFabSendCurrentLocation!!.setOnClickListener {
            try {
                SmsUtils.sendMyLocation(applicationContext, mCurrentLocation!!)
            } catch (e: Exception) {
                Utils.showErrorDialog(applicationContext, "Error", getString(R.string.fab_send_loc_error_text) + e.toString())
            }
        }

        mFabSetMeetLocation!!.setOnClickListener {
            if (mMeetupLocationMarker != null) {
                mMeetupLocationMarker!!.remove()
            }
            val tempMkLocation = LatLng(mCurrentLocation!!.latitude + 0.0010, mCurrentLocation!!.longitude + 0.0010)
            mMeetupLocationMarker = mMap!!.addMarker(MarkerOptions()
                    .position(tempMkLocation)
                    .icon(BitmapUtils.getMarkerIconFromDrawable(applicationContext, R.drawable.meetup_marker))
                    .draggable(true)
                    .snippet("Tap to send meet-up location")
                    .title("Meet-up location"))
            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(tempMkLocation, 18f))
            closeFABMenu()
        }
    }

    private fun showFABMenu() {
        mIsFabMenuOpen = true
        mFabLayout1!!.visibility = View.VISIBLE
        mFabLayout2!!.visibility = View.VISIBLE
        mFabLayout3!!.visibility = View.VISIBLE
        mFabOverlay!!.visibility = View.VISIBLE
        mFabMainMenu!!.animate().rotationBy(180f).duration = 300
        mFabLayout1!!.animate().translationY(-resources.getDimension(R.dimen.standard_55)).duration = 300
        mFabLayout2!!.animate().translationY(-resources.getDimension(R.dimen.standard_100)).duration = 320
        mFabLayout3!!.animate().translationY(-resources.getDimension(R.dimen.standard_145)).duration = 340
    }

    private fun closeFABMenu() {
        mIsFabMenuOpen = false
        mFabOverlay!!.visibility = View.GONE
        mFabMainMenu!!.animate().rotationBy(-180f)
        mFabLayout1!!.animate().translationY(0f)
        mFabLayout2!!.animate().translationY(0f)
        mFabLayout3!!.animate().translationY(0f).setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}
            override fun onAnimationEnd(animator: Animator) {
                if (!mIsFabMenuOpen) {
                    mFabLayout1!!.visibility = View.GONE
                    mFabLayout2!!.visibility = View.GONE
                    mFabLayout3!!.visibility = View.GONE
                }

            }

            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
    }

    private fun showFriendsAndMeetupLocs() {
        if (mMeetupLocationMarker != null) {
            val meetupLoc = mMeetupLocationMarker!!.position
            mMeetupLocationMarker!!.remove()
            mMeetupLocationMarker = mMap!!.addMarker(MarkerOptions()
                    .position(meetupLoc)
                    .icon(BitmapUtils.getMarkerIconFromDrawable(this, R.drawable.meetup_marker))
                    .title("Meet-up location"))
        }
        if (mFriendLocations != null && mFriendLocations!!.size != 0) {
            for (m in mFriendLocations!!) {
                val friendData = m.tag as Friend?
                val friendMarker = mMap!!.addMarker(MarkerOptions()
                        .position(m.position)
                        .icon(BitmapUtils.getMarkerIconFromDrawable(this, R.drawable.friend_marker))
                        .title(friendData!!.friendName))
                friendMarker.tag = friendData
            }
        }
    }

    internal inner class MainActivityBroadcastListener : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Constants.INTENT_RELOAD_SETTINGS) {
                reloadSettings(intent.getBooleanExtra("reinitApp", false))
            } else if (intent.action == Constants.INTENT_UPDATE_MEETUP_LOCATION) {
                val lat: Double
                val lon: Double
                val coordinates: LatLng
                lat = intent.getDoubleExtra("lat", 0.0)
                lon = intent.getDoubleExtra("lon", 0.0)
                coordinates = LatLng(lat, lon)
                if (lat != 0.0 && lon != 0.0) {
                    if (mMeetupLocationMarker != null) {
                        mMeetupLocationMarker!!.remove()
                    }
                    mMeetupLocationMarker = mMap!!.addMarker(MarkerOptions()
                            .position(coordinates)
                            .icon(BitmapUtils.getMarkerIconFromDrawable(context, R.drawable.meetup_marker))
                            .title("Meet-up location"))
                    mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15f))
                }
            } else if (intent.action == Constants.INTENT_ADD_FRIEND_CURRENT_LOCATION) {
                val lat: Double
                val lon: Double
                val friendName: String
                val friendNumber: String
                val coordinates: LatLng
                lat = intent.getDoubleExtra("lat", 0.0)
                lon = intent.getDoubleExtra("lon", 0.0)
                friendName = intent.getStringExtra("name")
                friendNumber = intent.getStringExtra("number")
                coordinates = LatLng(lat, lon)

                if (mFriendLocations == null) {
                    mFriendLocations = ArrayList()
                }

                if (mFriendLocations!!.isEmpty()) {
                    val friendMarker = mMap!!.addMarker(MarkerOptions()
                            .position(coordinates)
                            .icon(BitmapUtils.getMarkerIconFromDrawable(context, R.drawable.friend_marker))
                            .title(friendName))
                    friendMarker.tag = Friend(friendName, friendNumber)
                    mFriendLocations!!.add(friendMarker)
                } else {
                    var found = false
                    for (i in mFriendLocations!!.indices) {
                        val friendData = mFriendLocations!![i].tag as Friend?
                        if (friendData!!.friendName == friendName && friendData.friendMobileNumber == friendNumber) {
                            mFriendLocations!![i].position = LatLng(lat, lon)
                            found = true
                            break
                        }
                    }
                    if (!found) {
                        val friendMarker = mMap!!.addMarker(MarkerOptions()
                                .position(coordinates)
                                .icon(BitmapUtils.getMarkerIconFromDrawable(context, R.drawable.friend_marker))
                                .title(friendName))
                        friendMarker.tag = Friend(friendName, friendNumber)
                        mFriendLocations!!.add(friendMarker)
                    }
                }
            }
        }
    }

    companion object {
        private val TAG = "London Meetup [Main]"
        private val INTERVAL = (1000 * 10).toLong()
        private val FASTEST_INTERVAL = (1000 * 5).toLong()
    }
}