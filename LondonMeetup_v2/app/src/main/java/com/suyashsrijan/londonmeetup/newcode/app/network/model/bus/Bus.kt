package com.suyashsrijan.londonmeetup.newcode.app.network.model.bus

import com.google.android.gms.maps.model.LatLng
import com.suyashsrijan.londonmeetup.API.tfl.enums.Stoptypes

class Bus(val stopType: Stoptypes, val stopName: String, val stopIndicator: String, val stopId: String, val coordinates: LatLng)
