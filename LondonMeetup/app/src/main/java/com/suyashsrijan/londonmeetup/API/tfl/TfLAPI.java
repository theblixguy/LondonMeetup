package com.suyashsrijan.londonmeetup.API.tfl;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.suyashsrijan.londonmeetup.API.tfl.enums.Modes;
import com.suyashsrijan.londonmeetup.API.tfl.enums.Stoptypes;
import com.suyashsrijan.londonmeetup.API.tfl.interfaces.ApiResultCallback;
import com.suyashsrijan.londonmeetup.API.tfl.interfaces.HttpGetResultCallback;
import com.suyashsrijan.londonmeetup.API.tfl.interfaces.ParseResultCallback;
import com.suyashsrijan.londonmeetup.API.tfl.models.Bike;
import com.suyashsrijan.londonmeetup.API.tfl.models.Bus;
import com.suyashsrijan.londonmeetup.API.tfl.models.BusDeparture;
import com.suyashsrijan.londonmeetup.API.tfl.models.Tube;
import com.suyashsrijan.londonmeetup.API.tfl.models.TubeDeparture;
import com.suyashsrijan.londonmeetup.API.tfl.networking.HttpGetRequest;
import com.suyashsrijan.londonmeetup.API.tfl.parsers.ParseBusDepartures;
import com.suyashsrijan.londonmeetup.API.tfl.parsers.ParseNearbyBikePoints;
import com.suyashsrijan.londonmeetup.API.tfl.parsers.ParseNearbyBusStops;
import com.suyashsrijan.londonmeetup.API.tfl.parsers.ParseNearbyTubeStops;
import com.suyashsrijan.londonmeetup.API.tfl.parsers.ParseTubeDepartures;
import com.suyashsrijan.londonmeetup.API.tfl.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TfLAPI {

    private Context context;
    private static final String TAG = "London Meetup [TfLAPI]";

    public TfLAPI(Context context) {
        this.context = context;
    }

    public void getNearbyTubes(int radius, LatLng coordinates, final ApiResultCallback<Tube> callback) {
        String stopTypeUrl = Utils.createNearbyTubeBusUrl(Stoptypes.METRO_STATION, radius, true, Modes.TUBE, true, coordinates);
        HttpGetRequest httpGetRequest = new HttpGetRequest(new HttpGetResultCallback() {
            @Override
            public void response(final String output) {
                ParseNearbyTubeStops parseNearbyTubeStops = new ParseNearbyTubeStops(new ParseResultCallback<Tube>() {
                    @Override
                    public void response(ArrayList<Tube> output) {
                        callback.response(context, output);
                    }
                    @Override
                    public void error(Exception exception) {
                        callback.error(context, exception);
                    }
                });
                try {
                    parseNearbyTubeStops.execute(new JSONObject(output));
                }
                catch (Exception e) {
                    callback.error(context, e);
                }
            }
            @Override
            public void error(Exception exception) {
                callback.error(context, exception);
            }
        });
        httpGetRequest.execute(stopTypeUrl);
    }

    public void getTubeDepartures(String stopId, final ApiResultCallback<TubeDeparture> callback) {
        String stopTypeDeparturesUrl = Utils.createTubeBusDeparturesUrl(stopId, Modes.TUBE);
        Log.i(TAG, stopTypeDeparturesUrl);
        HttpGetRequest httpGetRequest = new HttpGetRequest(new HttpGetResultCallback() {
            @Override
            public void response(final String output) {
                ParseTubeDepartures parseTubeDepartures = new ParseTubeDepartures(new ParseResultCallback<TubeDeparture>() {
                    @Override
                    public void response(ArrayList<TubeDeparture> output) {
                        callback.response(context, output);
                    }

                    @Override
                    public void error(Exception exception) {
                        callback.error(context, exception);
                    }
                });
                try {
                    parseTubeDepartures.execute(new JSONArray(output));
                }
                catch (Exception e) {
                    callback.error(context, e);
                }

            }

            @Override
            public void error(Exception exception) {
                callback.error(context, exception);
            }
        });
        httpGetRequest.execute(stopTypeDeparturesUrl);
    }

    public void getNearbyBusStops(int radius, LatLng coordinates, final ApiResultCallback<Bus> callback) {
        String stopTypeUrl = Utils.createNearbyTubeBusUrl(Stoptypes.PUBLIC_BUS_COACH_TRAM, radius, true, Modes.BUS, true, coordinates);
        HttpGetRequest httpGetRequest = new HttpGetRequest(new HttpGetResultCallback() {
            @Override
            public void response(final String output) {
                ParseNearbyBusStops parseNearbyBusStops = new ParseNearbyBusStops(new ParseResultCallback<Bus>() {
                    @Override
                    public void response(ArrayList<Bus> output) {
                        callback.response(context, output);
                    }
                    @Override
                    public void error(Exception exception) {
                        callback.error(context, exception);
                    }
                });
                try {
                    parseNearbyBusStops.execute(new JSONObject(output));
                }
                catch (Exception e) {
                    callback.error(context, e);
                }
            }
            @Override
            public void error(Exception exception) {
                callback.error(context, exception);
            }
        });
        httpGetRequest.execute(stopTypeUrl);
    }

    public void getBusDepartures(String stopId, final ApiResultCallback<BusDeparture> callback) {
        String stopTypeDeparturesUrl = Utils.createTubeBusDeparturesUrl(stopId, Modes.BUS);
        Log.i(TAG, stopTypeDeparturesUrl);
        HttpGetRequest httpGetRequest = new HttpGetRequest(new HttpGetResultCallback() {
            @Override
            public void response(final String output) {
                ParseBusDepartures parseTubeDepartures = new ParseBusDepartures(new ParseResultCallback<BusDeparture>() {
                    @Override
                    public void response(ArrayList<BusDeparture> output) {
                        callback.response(context, output);
                    }

                    @Override
                    public void error(Exception exception) {
                        callback.error(context, exception);
                    }
                });
                try {
                    parseTubeDepartures.execute(new JSONArray(output));
                }
                catch (Exception e) {
                    callback.error(context, e);
                }

            }

            @Override
            public void error(Exception exception) {
                callback.error(context, exception);
            }
        });
        httpGetRequest.execute(stopTypeDeparturesUrl);
    }

    public void getNearbyBikePoints(int radius, LatLng coordinates, final ApiResultCallback<Bike> callback) {
        String placeTypeUrl = Utils.createNearbyBikesUrl(radius, coordinates);
        HttpGetRequest httpGetRequest = new HttpGetRequest(new HttpGetResultCallback() {
            @Override
            public void response(final String output) {
                ParseNearbyBikePoints parseNearbyBikePoints = new ParseNearbyBikePoints(new ParseResultCallback<Bike>() {
                    @Override
                    public void response(ArrayList<Bike> output) {
                        callback.response(context, output);
                    }
                    @Override
                    public void error(Exception exception) {
                        callback.error(context, exception);
                    }
                });
                try {
                    parseNearbyBikePoints.execute(new JSONObject(output));
                }
                catch (Exception e) {
                    callback.error(context, e);
                }
            }
            @Override
            public void error(Exception exception) {
                callback.error(context, exception);
            }
        });
        httpGetRequest.execute(placeTypeUrl);
    }
}
