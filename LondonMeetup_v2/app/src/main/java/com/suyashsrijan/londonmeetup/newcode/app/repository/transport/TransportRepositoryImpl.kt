package com.suyashsrijan.londonmeetup.newcode.app.repository.transport

import com.google.android.gms.maps.model.LatLng
import com.suyashsrijan.londonmeetup.API.tfl.enums.Contants
import com.suyashsrijan.londonmeetup.API.tfl.enums.Modes
import com.suyashsrijan.londonmeetup.API.tfl.enums.Stoptypes
import com.suyashsrijan.londonmeetup.newcode.app.network.api.TransportForLondonAPI
import com.suyashsrijan.londonmeetup.newcode.app.network.model.bike.BikeModel
import com.suyashsrijan.londonmeetup.newcode.app.network.model.bus.BusDepartureModel
import com.suyashsrijan.londonmeetup.newcode.app.network.model.bus.BusModel
import com.suyashsrijan.londonmeetup.newcode.app.network.model.tube.TubeDepartureModel
import com.suyashsrijan.londonmeetup.newcode.app.network.model.tube.TubeModel
import javax.inject.Inject

class TransportRepositoryImpl
@Inject constructor(private val transportForLondonAPI: TransportForLondonAPI) : TransportRepository {

    override fun getNearbyTubes(radius: Int, coordinates: LatLng): TubeModel? {
        return transportForLondonAPI.getNearbyTubes(Contants.APP_ID.toString(), Contants.APP_KEY.toString(),
                Stoptypes.METRO_STATION, radius,true, Modes.TUBE,
                "none", true, coordinates.latitude, coordinates.longitude)
                .execute()
                .body()
    }

    override fun getNearbyBuses(radius: Int, coordinates: LatLng): BusModel? {
        return transportForLondonAPI.getNearbyBusStops(Contants.APP_ID.toString(), Contants.APP_KEY.toString(),
                Stoptypes.PUBLIC_BUS_COACH_TRAM, radius,true, Modes.BUS,
                "none", true, coordinates.latitude, coordinates.longitude)
                .execute()
                .body()
    }

    override fun getNearbyBikeDockingStations(radius: Int, coordinates: LatLng): BikeModel? {
        return transportForLondonAPI.getNearbyBikes(Contants.APP_ID.toString(), Contants.APP_KEY.toString(),
                radius, coordinates.latitude, coordinates.longitude)
                .execute()
                .body()
    }

    override fun getTubeDepartures(stopId: String): TubeDepartureModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBusDepartures(stopId: String): BusDepartureModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}