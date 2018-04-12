package com.suyashsrijan.londonmeetup.newcode.main.view

import android.app.Activity
import com.suyashsrijan.londonmeetup.newcode.app.model.Bike
import com.suyashsrijan.londonmeetup.newcode.app.model.Bus
import com.suyashsrijan.londonmeetup.newcode.app.model.Tube
import com.suyashsrijan.londonmeetup.newcode.main.di.MainComponent

interface MainView {
    fun injectDependencies(mainComponent: MainComponent)
    fun displayNearbyTubes(tubesList: List<Tube>)
    fun displayNearbyBuses(busesList: List<Bus>)
    fun displayNearbyBikes(bikesList: List<Bike>)
    fun displayBikeBottomSheet(bikeStationName: String, bikesAvailable: Int, bikesTaken: Int)
    fun getActivity() : Activity
}