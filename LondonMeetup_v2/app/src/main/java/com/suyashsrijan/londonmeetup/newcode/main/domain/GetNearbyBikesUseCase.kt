package com.suyashsrijan.londonmeetup.newcode.main.domain

import com.google.android.gms.maps.model.LatLng
import com.suyashsrijan.londonmeetup.newcode.app.domain.BaseUseCase
import com.suyashsrijan.londonmeetup.newcode.app.model.Bike
import com.suyashsrijan.londonmeetup.newcode.app.network.model.bike.BikeModel
import com.suyashsrijan.londonmeetup.newcode.app.repository.transport.TransportRepository
import kotlinx.coroutines.experimental.async
import javax.inject.Inject

class GetNearbyBikesUseCase
@Inject constructor(private val transportRepository: TransportRepository) : BaseUseCase() {

    suspend fun execute(radius: Int, coordinates: LatLng): List<Bike> {
        val nearbyBikes: BikeModel? = async {
            transportRepository.getNearbyBikeDockingStations(radius, coordinates)
        }.await()

        return mapBikeModelDataToBikeArray(nearbyBikes)
    }

    private fun mapBikeModelDataToBikeArray(model: BikeModel?) : MutableList<Bike> {
        val bikesList: MutableList<Bike> = mutableListOf()
        model?.places?.forEach { place ->
            bikesList.add(Bike(place.commonName, LatLng(place.lat!!, place.lon!!), place.additionalProperties?.get(6)?.value?.toInt(),  place.additionalProperties?.get(7)?.value?.toInt(),  place.additionalProperties?.get(8)?.value?.toInt()))
        }
        return bikesList
    }


}