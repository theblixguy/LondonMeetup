package com.suyashsrijan.londonmeetup.newcode.main.presenter

import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.suyashsrijan.londonmeetup.activities.BusDeparturesActivity
import com.suyashsrijan.londonmeetup.activities.TubeDeparturesActivity
import com.suyashsrijan.londonmeetup.models.BikeMarkerData
import com.suyashsrijan.londonmeetup.models.BusMarkerData
import com.suyashsrijan.londonmeetup.models.TubeMarkerData
import com.suyashsrijan.londonmeetup.newcode.app.App
import com.suyashsrijan.londonmeetup.newcode.app.model.Bike
import com.suyashsrijan.londonmeetup.newcode.app.model.Bus
import com.suyashsrijan.londonmeetup.newcode.app.model.Tube
import com.suyashsrijan.londonmeetup.newcode.app.presenter.BasePresenterImpl
import com.suyashsrijan.londonmeetup.newcode.main.di.MainComponent
import com.suyashsrijan.londonmeetup.newcode.main.di.MainModule
import com.suyashsrijan.londonmeetup.newcode.main.domain.GetNearbyBikesUseCase
import com.suyashsrijan.londonmeetup.newcode.main.domain.GetNearbyBusesUseCase
import com.suyashsrijan.londonmeetup.newcode.main.domain.GetNearbyTubesUseCase
import com.suyashsrijan.londonmeetup.newcode.main.view.MainView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class MainPresenterImpl : BasePresenterImpl<MainView>(), MainPresenter<MainView> {

    @Inject
    internal lateinit var getNearbyTubesUseCase: GetNearbyTubesUseCase
    @Inject
    internal lateinit var getNearbyBusesUseCase: GetNearbyBusesUseCase
    @Inject
    internal lateinit var getNearbyBikesUseCase: GetNearbyBikesUseCase

    private var mainComponent: MainComponent? = null

    private val tubes: MutableList<Tube> = mutableListOf()
    private val buses: MutableList<Bus> = mutableListOf()
    private val bikes: MutableList<Bike> = mutableListOf()

    init {
        injectDependencies()
    }

    override fun onInjectDependencies() {
        mainComponent = App.get()
                .getAppComponent()
                ?.plus(MainModule())

        mainComponent?.inject(this)
    }

    override fun onViewAttached(view: MainView) {
        mainComponent?.let { view.injectDependencies(it) }
    }

    override val tubesList: List<Tube>
        get() = tubes

    override val busesList: List<Bus>
        get() = buses

    override val bikesList: List<Bike>
        get() = bikes

    private suspend fun updateNearbyTubes(tubesList: List<Tube>) {
        tubes.clear()
        tubes.addAll(tubesList)
        getView()?.displayNearbyTubes(tubes)
    }

    private suspend fun updateNearbyBuses(busesList: List<Bus>) {
        buses.clear()
        buses.addAll(busesList)
        getView()?.displayNearbyBuses(buses)
    }

    private suspend fun updateNearbyBikes(bikesList: List<Bike>) {
        bikes.clear()
        bikes.addAll(bikesList)
        getView()?.displayNearbyBikes(bikes)
    }
    override fun getNearbyTubes(radius: Int, coordinates: LatLng) {
        launch(UI) {
            updateNearbyTubes(getNearbyTubesUseCase.execute(radius, coordinates))
        }
    }

    override fun getNearbyBuses(radius: Int, coordinates: LatLng) {
        launch(UI) {
            updateNearbyBuses(getNearbyBusesUseCase.execute(radius, coordinates))
        }
    }

    override fun getNearbyBikes(radius: Int, coordinates: LatLng) {
        launch(UI) {
            updateNearbyBikes(getNearbyBikesUseCase.execute(radius, coordinates))
        }
    }

    override fun handleInfoWindowClick(marker: Marker) {
        if (marker.tag is TubeMarkerData) {
            val intent = Intent(getView()?.getActivity(), TubeDeparturesActivity::class.java)
            val markerData = marker.tag as TubeMarkerData?
            intent.putExtra("stopId", markerData!!.stopId)
            startActivity(getView()?.getActivity()!!, intent, null)
        } else if (marker.tag is BusMarkerData) {
            val intent = Intent(getView()?.getActivity(), BusDeparturesActivity::class.java)
            val markerData = marker.tag as BusMarkerData?
            intent.putExtra("stopId", markerData!!.stopId)
            startActivity(getView()?.getActivity()!!, intent, null)
        } else if (marker.tag is BikeMarkerData) {
            val markerData = marker.tag as BikeMarkerData?
            getView()?.displayBikeBottomSheet(markerData!!.stopName, markerData.bikesAvailable, markerData.bikesTaken)
        }
    }
}