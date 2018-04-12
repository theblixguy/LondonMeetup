package com.suyashsrijan.londonmeetup.newcode.app.network.model.bike

import com.google.android.gms.maps.model.LatLng
import com.suyashsrijan.londonmeetup.API.tfl.enums.Placetypes

class Bike(val placeType: Placetypes, val stopName: String, val bikesAvailable: Int, val bikesTaken: Int, val totalBikes: Int, val coordinates: LatLng)
