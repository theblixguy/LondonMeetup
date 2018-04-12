package com.suyashsrijan.londonmeetup.newcode.main.domain

import com.google.android.gms.maps.model.LatLng
import com.suyashsrijan.londonmeetup.newcode.app.domain.BaseUseCase
import com.suyashsrijan.londonmeetup.newcode.app.model.Bus
import com.suyashsrijan.londonmeetup.newcode.app.network.model.bus.BusModel
import com.suyashsrijan.londonmeetup.newcode.app.repository.transport.TransportRepository
import kotlinx.coroutines.experimental.async
import javax.inject.Inject

class GetNearbyBusesUseCase
@Inject constructor(private val transportRepository: TransportRepository) : BaseUseCase() {

    suspend fun execute(radius: Int, coordinates: LatLng): List<Bus> {
        val nearbyBuses: BusModel? = async {
            transportRepository.getNearbyBuses(radius, coordinates)
        }.await()

        return mapBusModelDataToBusArray(nearbyBuses)
    }

    private fun mapBusModelDataToBusArray(model: BusModel?) : MutableList<Bus> {
        val busesList: MutableList<Bus> = mutableListOf()
        model?.stopPoints?.forEach { point ->
            busesList.add(Bus(point.commonName, point.indicator, LatLng(point.lat!!, point.lon!!), point.id))
        }
        return busesList
    }


}