package com.suyashsrijan.londonmeetup.newcode.main.domain

import com.google.android.gms.maps.model.LatLng
import com.suyashsrijan.londonmeetup.newcode.app.domain.BaseUseCase
import com.suyashsrijan.londonmeetup.newcode.app.model.Tube
import com.suyashsrijan.londonmeetup.newcode.app.network.model.tube.TubeModel
import com.suyashsrijan.londonmeetup.newcode.app.repository.transport.TransportRepository
import kotlinx.coroutines.experimental.async
import javax.inject.Inject

class GetNearbyTubesUseCase
@Inject constructor(private val transportRepository: TransportRepository) : BaseUseCase() {

    suspend fun execute(radius: Int, coordinates: LatLng): List<Tube> {
        val nearbyTubes: TubeModel? = async {
            transportRepository.getNearbyTubes(radius, coordinates)
        }.await()

        return mapTubeModelDataToTubeArray(nearbyTubes)
    }

    private fun mapTubeModelDataToTubeArray(model: TubeModel?) : MutableList<Tube> {
        val tubesList: MutableList<Tube> = mutableListOf()
        model?.stopPoints?.forEach { point ->
            tubesList.add(Tube(point.commonName, LatLng(point.lat!!, point.lon!!), point.id))
        }
        return tubesList
    }


}