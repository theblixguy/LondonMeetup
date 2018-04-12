package com.suyashsrijan.londonmeetup.API.tfl.parsers

import android.os.AsyncTask

import com.google.android.gms.maps.model.LatLng
import com.suyashsrijan.londonmeetup.API.tfl.interfaces.ParseResultCallback
import com.suyashsrijan.londonmeetup.newcode.app.network.model.bike.Bike
import com.suyashsrijan.londonmeetup.API.tfl.utils.Utils

import org.json.JSONObject

import java.util.ArrayList

class ParseNearbyBikePoints(callback: ParseResultCallback<Bike>) : AsyncTask<JSONObject, Void, ArrayList<Bike>>() {

    private var delegate: ParseResultCallback<Bike>? = null
    private var exception: Exception? = null

    init {
        this.delegate = callback
    }

    override fun doInBackground(vararg params: JSONObject): ArrayList<Bike> {
        val bikePointData = params[0]
        val listofPoints = ArrayList<Bike>()
        try {
            val stopPointsArray = bikePointData.getJSONArray("places")
            for (i in 0 until stopPointsArray.length()) {
                val bikePointObject = stopPointsArray.getJSONObject(i)
                val bikePointAdditionalProps = bikePointObject.getJSONArray("additionalProperties")
                listofPoints.add(Bike(Utils.getPlacetypeEnum(bikePointObject.getString("placeType"))!!, bikePointObject.getString("commonName"), bikePointAdditionalProps.getJSONObject(6).getInt("value"), bikePointAdditionalProps.getJSONObject(7).getInt("value"), bikePointAdditionalProps.getJSONObject(8).getInt("value"), LatLng(bikePointObject.getDouble("lat"), bikePointObject.getDouble("lon"))))
            }
        } catch (e: Exception) {
            exception = e
        }

        return listofPoints
    }

    override fun onPostExecute(result: ArrayList<Bike>) {
        if (exception != null) {
            delegate!!.error(exception!!)
        } else {
            delegate!!.response(result)
        }
    }
}
