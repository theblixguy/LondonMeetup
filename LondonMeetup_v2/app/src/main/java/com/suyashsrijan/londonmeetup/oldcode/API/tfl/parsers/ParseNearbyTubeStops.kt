package com.suyashsrijan.londonmeetup.API.tfl.parsers

import android.os.AsyncTask

import com.google.android.gms.maps.model.LatLng
import com.suyashsrijan.londonmeetup.API.tfl.interfaces.ParseResultCallback
import com.suyashsrijan.londonmeetup.newcode.app.network.model.tube.Tube
import com.suyashsrijan.londonmeetup.API.tfl.utils.Utils

import org.json.JSONObject

import java.util.ArrayList

class ParseNearbyTubeStops(callback: ParseResultCallback<Tube>) : AsyncTask<JSONObject, Void, ArrayList<Tube>>() {

    private var delegate: ParseResultCallback<Tube>? = null
    private var exception: Exception? = null

    init {
        this.delegate = callback
    }

    override fun doInBackground(vararg params: JSONObject): ArrayList<Tube> {
        val stopPointData = params[0]
        val listofStops = ArrayList<Tube>()
        try {
            val stopPointsArray = stopPointData.getJSONArray("stopPoints")
            for (i in 0 until stopPointsArray.length()) {
                val stopPointObject = stopPointsArray.getJSONObject(i)
                listofStops.add(Tube(Utils.getStoptypeEnum(stopPointObject.getString("stopType"))!!, stopPointObject.getString("commonName"), stopPointObject.getString("id"), LatLng(stopPointObject.getDouble("lat"), stopPointObject.getDouble("lon"))))
            }
        } catch (e: Exception) {
            exception = e
        }

        return listofStops
    }

    override fun onPostExecute(result: ArrayList<Tube>) {
        if (exception != null) {
            delegate!!.error(exception!!)
        } else {
            delegate!!.response(result)
        }
    }
}
