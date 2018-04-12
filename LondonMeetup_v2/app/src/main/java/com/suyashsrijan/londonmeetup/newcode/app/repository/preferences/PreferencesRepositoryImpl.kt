package com.suyashsrijan.londonmeetup.newcode.app.repository.preferences

import android.content.Context
import android.preference.PreferenceManager
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class PreferencesRepositoryImpl
@Inject constructor(private val context: Context) : PreferencesRepository {

    override fun getMaximumRadius(): Int {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("stopsMaxRadiusPreference", 2) * 100
    }

    override fun shouldShowNearbyTubes(): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("showTubeStopsPreference", true)
    }

    override fun shouldShowNearbyBuses(): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("showBusStopsPreference", true)
    }

    override fun shouldShowNearbyBikeDockingStations(): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("showCycleStopsPreference", true)
    }

    override fun shouldUseMockLocation(): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("useMockLocationPreference", false)
    }

    override fun getMockLocation(): LatLng {
        val latitude: Double = PreferenceManager.getDefaultSharedPreferences(context).getFloat("mockLocationLat", 0.0f).toDouble()
        val longitude: Double = PreferenceManager.getDefaultSharedPreferences(context).getFloat("mockLocationLon", 0.0f).toDouble()
        return LatLng(latitude, longitude)
    }
}