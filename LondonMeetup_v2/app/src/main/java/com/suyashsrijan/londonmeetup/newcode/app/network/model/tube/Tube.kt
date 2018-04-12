package com.suyashsrijan.londonmeetup.newcode.app.network.model.tube

import com.google.android.gms.maps.model.LatLng
import com.suyashsrijan.londonmeetup.API.tfl.enums.Stoptypes

class Tube(val stopType: Stoptypes, val stopName: String, val stopId: String, val coordinates: LatLng)
