package com.suyashsrijan.londonmeetup.newcode.app.repository.transport

import com.google.android.gms.maps.model.LatLng
import com.suyashsrijan.londonmeetup.newcode.app.network.model.bike.BikeModel
import com.suyashsrijan.londonmeetup.newcode.app.network.model.bus.BusDepartureModel
import com.suyashsrijan.londonmeetup.newcode.app.network.model.bus.BusModel
import com.suyashsrijan.londonmeetup.newcode.app.network.model.tube.TubeDepartureModel
import com.suyashsrijan.londonmeetup.newcode.app.network.model.tube.TubeModel

interface TransportRepository {

    fun getNearbyTubes(radius: Int, coordinates: LatLng): TubeModel?
    fun getNearbyBuses(radius: Int, coordinates: LatLng): BusModel?
    fun getNearbyBikeDockingStations(radius: Int, coordinates: LatLng): BikeModel?
    fun getTubeDepartures(stopId: String): TubeDepartureModel?
    fun getBusDepartures(stopId: String): BusDepartureModel?

}