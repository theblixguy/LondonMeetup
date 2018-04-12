package com.suyashsrijan.londonmeetup.oldcode.API.tfl

import android.content.Context
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.suyashsrijan.londonmeetup.API.tfl.enums.Modes
import com.suyashsrijan.londonmeetup.API.tfl.enums.Stoptypes
import com.suyashsrijan.londonmeetup.API.tfl.interfaces.ApiResultCallback
import com.suyashsrijan.londonmeetup.API.tfl.interfaces.HttpGetResultCallback
import com.suyashsrijan.londonmeetup.API.tfl.interfaces.ParseResultCallback
import com.suyashsrijan.londonmeetup.API.tfl.networking.HttpGetRequest
import com.suyashsrijan.londonmeetup.API.tfl.parsers.*
import com.suyashsrijan.londonmeetup.API.tfl.utils.Utils
import com.suyashsrijan.londonmeetup.newcode.app.network.model.bike.Bike
import com.suyashsrijan.londonmeetup.newcode.app.network.model.bus.Bus
import com.suyashsrijan.londonmeetup.newcode.app.network.model.bus.BusDeparture
import com.suyashsrijan.londonmeetup.newcode.app.network.model.tube.Tube
import com.suyashsrijan.londonmeetup.newcode.app.network.model.tube.TubeDeparture
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class TfLAPI(private val context: Context) {

    fun getNearbyTubes(radius: Int, coordinates: LatLng, callback: ApiResultCallback<Tube>) {
        val stopTypeUrl = Utils.createNearbyTubeBusUrl(Stoptypes.METRO_STATION, radius, true, Modes.TUBE, true, coordinates)
        val httpGetRequest = HttpGetRequest(object : HttpGetResultCallback {
            override fun response(output: String) {
                val parseNearbyTubeStops = ParseNearbyTubeStops(object : ParseResultCallback<Tube> {
                    override fun response(output: ArrayList<Tube>) {
                        callback.response(context, output)
                    }

                    override fun error(exception: Exception) {
                        callback.error(context, exception)
                    }
                })
                try {
                    parseNearbyTubeStops.execute(JSONObject(output))
                } catch (e: Exception) {
                    callback.error(context, e)
                }

            }

            override fun error(exception: Exception) {
                callback.error(context, exception)
            }
        })
        httpGetRequest.execute(stopTypeUrl)
    }

    fun getTubeDepartures(stopId: String, callback: ApiResultCallback<TubeDeparture>) {
        val stopTypeDeparturesUrl = Utils.createTubeBusDeparturesUrl(stopId, Modes.TUBE)
        Log.i(TAG, stopTypeDeparturesUrl)
        val httpGetRequest = HttpGetRequest(object : HttpGetResultCallback {
            override fun response(output: String) {
                val parseTubeDepartures = ParseTubeDepartures(object : ParseResultCallback<TubeDeparture> {
                    override fun response(output: ArrayList<TubeDeparture>) {
                        callback.response(context, output)
                    }

                    override fun error(exception: Exception) {
                        callback.error(context, exception)
                    }
                })
                try {
                    parseTubeDepartures.execute(JSONArray(output))
                } catch (e: Exception) {
                    callback.error(context, e)
                }

            }

            override fun error(exception: Exception) {
                callback.error(context, exception)
            }
        })
        httpGetRequest.execute(stopTypeDeparturesUrl)
    }

    fun getNearbyBusStops(radius: Int, coordinates: LatLng, callback: ApiResultCallback<Bus>) {
        val stopTypeUrl = Utils.createNearbyTubeBusUrl(Stoptypes.PUBLIC_BUS_COACH_TRAM, radius, true, Modes.BUS, true, coordinates)
        val httpGetRequest = HttpGetRequest(object : HttpGetResultCallback {
            override fun response(output: String) {
                val parseNearbyBusStops = ParseNearbyBusStops(object : ParseResultCallback<Bus> {
                    override fun response(output: ArrayList<Bus>) {
                        callback.response(context, output)
                    }

                    override fun error(exception: Exception) {
                        callback.error(context, exception)
                    }
                })
                try {
                    parseNearbyBusStops.execute(JSONObject(output))
                } catch (e: Exception) {
                    callback.error(context, e)
                }

            }

            override fun error(exception: Exception) {
                callback.error(context, exception)
            }
        })
        httpGetRequest.execute(stopTypeUrl)
    }

    fun getBusDepartures(stopId: String, callback: ApiResultCallback<BusDeparture>) {
        val stopTypeDeparturesUrl = Utils.createTubeBusDeparturesUrl(stopId, Modes.BUS)
        Log.i(TAG, stopTypeDeparturesUrl)
        val httpGetRequest = HttpGetRequest(object : HttpGetResultCallback {
            override fun response(output: String) {
                val parseTubeDepartures = ParseBusDepartures(object : ParseResultCallback<BusDeparture> {
                    override fun response(output: ArrayList<BusDeparture>) {
                        callback.response(context, output)
                    }

                    override fun error(exception: Exception) {
                        callback.error(context, exception)
                    }
                })
                try {
                    parseTubeDepartures.execute(JSONArray(output))
                } catch (e: Exception) {
                    callback.error(context, e)
                }

            }

            override fun error(exception: Exception) {
                callback.error(context, exception)
            }
        })
        httpGetRequest.execute(stopTypeDeparturesUrl)
    }

    fun getNearbyBikePoints(radius: Int, coordinates: LatLng, callback: ApiResultCallback<Bike>) {
        val placeTypeUrl = Utils.createNearbyBikesUrl(radius, coordinates)
        val httpGetRequest = HttpGetRequest(object : HttpGetResultCallback {
            override fun response(output: String) {
                val parseNearbyBikePoints = ParseNearbyBikePoints(object : ParseResultCallback<Bike> {
                    override fun response(output: ArrayList<Bike>) {
                        callback.response(context, output)
                    }

                    override fun error(exception: Exception) {
                        callback.error(context, exception)
                    }
                })
                try {
                    parseNearbyBikePoints.execute(JSONObject(output))
                } catch (e: Exception) {
                    callback.error(context, e)
                }

            }

            override fun error(exception: Exception) {
                callback.error(context, exception)
            }
        })
        httpGetRequest.execute(placeTypeUrl)
    }

    companion object {
        private val TAG = "London Meetup [TfLAPI]"
    }
}
