package com.suyashsrijan.londonmeetup.newcode.main.presenter

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.suyashsrijan.londonmeetup.newcode.app.model.Bike
import com.suyashsrijan.londonmeetup.newcode.app.model.Bus
import com.suyashsrijan.londonmeetup.newcode.app.model.Tube
import com.suyashsrijan.londonmeetup.newcode.app.presenter.BasePresenter

interface MainPresenter<ViewInterface> : BasePresenter<ViewInterface> {

    val tubesList: List<Tube>
    val busesList: List<Bus>
    val bikesList: List<Bike>

    fun getNearbyTubes(radius: Int, coordinates: LatLng)
    fun getNearbyBuses(radius: Int, coordinates: LatLng)
    fun getNearbyBikes(radius: Int, coordinates: LatLng)

    fun handleInfoWindowClick(marker: Marker)



}