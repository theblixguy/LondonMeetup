package com.suyashsrijan.londonmeetup.newcode.app.repository.preferences

import com.google.android.gms.maps.model.LatLng

interface PreferencesRepository {
    fun getMaximumRadius(): Int
    fun shouldShowNearbyTubes(): Boolean
    fun shouldShowNearbyBuses(): Boolean
    fun shouldShowNearbyBikeDockingStations(): Boolean
    fun shouldUseMockLocation(): Boolean
    fun getMockLocation(): LatLng
}