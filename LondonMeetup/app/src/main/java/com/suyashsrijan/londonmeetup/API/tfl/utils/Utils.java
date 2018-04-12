package com.suyashsrijan.londonmeetup.API.tfl.utils;

import com.google.android.gms.maps.model.LatLng;
import com.suyashsrijan.londonmeetup.API.tfl.enums.Contants;
import com.suyashsrijan.londonmeetup.API.tfl.enums.Modes;
import com.suyashsrijan.londonmeetup.API.tfl.enums.Placetypes;
import com.suyashsrijan.londonmeetup.API.tfl.enums.Stoptypes;

public class Utils {

    public static String createNearbyTubeBusUrl(Stoptypes stoptype, int radius, boolean useStopPointHierarchy, Modes mode, boolean returnLines, LatLng coordinates) {
        StringBuilder UrlBuilder = new StringBuilder();
        UrlBuilder.append(Contants.BASE_URL_STOPPOINT);
        UrlBuilder.append("?app_id=").append(Contants.APP_ID);
        UrlBuilder.append("&app_key=").append(Contants.APP_KEY);
        UrlBuilder.append("&stoptypes=").append(stoptype);
        UrlBuilder.append("&radius=").append(radius);
        UrlBuilder.append("&useStopPointHierarchy=").append(useStopPointHierarchy);
        UrlBuilder.append("&modes=").append(mode);
        UrlBuilder.append("&categories=none");
        UrlBuilder.append("&returnLines=").append(returnLines);
        UrlBuilder.append("&lat=").append(coordinates.latitude);
        UrlBuilder.append("&lon=").append(coordinates.longitude);
        return UrlBuilder.toString();
    }

    public static String createNearbyBikesUrl(int radius, LatLng coordinates) {
        StringBuilder UrlBuilder = new StringBuilder();
        UrlBuilder.append(Contants.BASE_URL_BIKES);
        UrlBuilder.append("?app_id=").append(Contants.APP_ID);
        UrlBuilder.append("&app_key=").append(Contants.APP_KEY);
        UrlBuilder.append("&radius=").append(radius);
        UrlBuilder.append("&lat=").append(coordinates.latitude);
        UrlBuilder.append("&lon=").append(coordinates.longitude);
        return UrlBuilder.toString();
    }

    public static String createTubeBusDeparturesUrl(String stopId, Modes mode) {
        StringBuilder UrlBuilder = new StringBuilder();
        UrlBuilder.append(Contants.BASE_URL_STOPPOINT);
        UrlBuilder.append("/").append(stopId);
        UrlBuilder.append("/Arrivals");
        UrlBuilder.append("?app_id=").append(Contants.APP_ID);
        UrlBuilder.append("&app_key=").append(Contants.APP_KEY);
        UrlBuilder.append("&mode=").append(mode);
        return UrlBuilder.toString();
    }

    public static Stoptypes getStoptypeEnum(String stopTypeName) {
        Stoptypes stoptype = null;
        if (stopTypeName.equalsIgnoreCase("NaptanMetroStation")) {
            stoptype = Stoptypes.METRO_STATION;
        } else if (stopTypeName.equalsIgnoreCase("NaptanPublicBusCoachTram")) {
            stoptype = Stoptypes.PUBLIC_BUS_COACH_TRAM;
        }
        return stoptype;
    }

    public static Placetypes getPlacetypeEnum(String placeTypeName) {
        Placetypes placetype = null;
        if (placeTypeName.equalsIgnoreCase("BikePoint")) {
            placetype =  Placetypes.BIKE_POINT;
        }
        return placetype;
    }
}
