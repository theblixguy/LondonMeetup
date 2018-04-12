package com.suyashsrijan.londonmeetup.API.tfl.utils

import com.google.android.gms.maps.model.LatLng
import com.suyashsrijan.londonmeetup.API.tfl.enums.Contants
import com.suyashsrijan.londonmeetup.API.tfl.enums.Modes
import com.suyashsrijan.londonmeetup.API.tfl.enums.Placetypes
import com.suyashsrijan.londonmeetup.API.tfl.enums.Stoptypes

object Utils {

    fun createNearbyTubeBusUrl(stoptype: Stoptypes, radius: Int, useStopPointHierarchy: Boolean, mode: Modes, returnLines: Boolean, coordinates: LatLng): String {
        val UrlBuilder = StringBuilder()
        UrlBuilder.append(Contants.BASE_URL_STOPPOINT)
        UrlBuilder.append("?app_id=").append(Contants.APP_ID)
        UrlBuilder.append("&app_key=").append(Contants.APP_KEY)
        UrlBuilder.append("&stoptypes=").append(stoptype)
        UrlBuilder.append("&radius=").append(radius)
        UrlBuilder.append("&useStopPointHierarchy=").append(useStopPointHierarchy)
        UrlBuilder.append("&modes=").append(mode)
        UrlBuilder.append("&categories=none")
        UrlBuilder.append("&returnLines=").append(returnLines)
        UrlBuilder.append("&lat=").append(coordinates.latitude)
        UrlBuilder.append("&lon=").append(coordinates.longitude)
        return UrlBuilder.toString()
    }

    fun createNearbyBikesUrl(radius: Int, coordinates: LatLng): String {
        val UrlBuilder = StringBuilder()
        UrlBuilder.append(Contants.BASE_URL_BIKES)
        UrlBuilder.append("?app_id=").append(Contants.APP_ID)
        UrlBuilder.append("&app_key=").append(Contants.APP_KEY)
        UrlBuilder.append("&radius=").append(radius)
        UrlBuilder.append("&lat=").append(coordinates.latitude)
        UrlBuilder.append("&lon=").append(coordinates.longitude)
        return UrlBuilder.toString()
    }

    fun createTubeBusDeparturesUrl(stopId: String, mode: Modes): String {
        val UrlBuilder = StringBuilder()
        UrlBuilder.append(Contants.BASE_URL_STOPPOINT)
        UrlBuilder.append("/").append(stopId)
        UrlBuilder.append("/Arrivals")
        UrlBuilder.append("?app_id=").append(Contants.APP_ID)
        UrlBuilder.append("&app_key=").append(Contants.APP_KEY)
        UrlBuilder.append("&mode=").append(mode)
        return UrlBuilder.toString()
    }

    fun getStoptypeEnum(stopTypeName: String): Stoptypes? {
        var stoptype: Stoptypes? = null
        if (stopTypeName.equals("NaptanMetroStation", ignoreCase = true)) {
            stoptype = Stoptypes.METRO_STATION
        } else if (stopTypeName.equals("NaptanPublicBusCoachTram", ignoreCase = true)) {
            stoptype = Stoptypes.PUBLIC_BUS_COACH_TRAM
        }
        return stoptype
    }

    fun getPlacetypeEnum(placeTypeName: String): Placetypes? {
        var placetype: Placetypes? = null
        if (placeTypeName.equals("BikePoint", ignoreCase = true)) {
            placetype = Placetypes.BIKE_POINT
        }
        return placetype
    }
}
