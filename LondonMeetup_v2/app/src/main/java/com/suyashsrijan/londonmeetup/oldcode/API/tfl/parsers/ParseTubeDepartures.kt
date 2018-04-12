package com.suyashsrijan.londonmeetup.API.tfl.parsers

import android.os.AsyncTask

import com.suyashsrijan.londonmeetup.API.tfl.interfaces.ParseResultCallback
import com.suyashsrijan.londonmeetup.newcode.app.network.model.tube.TubeDeparture

import org.json.JSONArray

import java.util.ArrayList

class ParseTubeDepartures(callback: ParseResultCallback<TubeDeparture>) : AsyncTask<JSONArray, Void, ArrayList<TubeDeparture>>() {

    private var delegate: ParseResultCallback<TubeDeparture>? = null
    private var exception: Exception? = null

    init {
        this.delegate = callback
    }

    override fun doInBackground(vararg params: JSONArray): ArrayList<TubeDeparture> {
        val stopPointsDepartureArray = params[0]
        val listofDepartures = ArrayList<TubeDeparture>()
        try {
            for (i in 0 until stopPointsDepartureArray.length()) {
                val stopPointDepartureObject = stopPointsDepartureArray.getJSONObject(i)
                listofDepartures.add(TubeDeparture(stopPointDepartureObject.getString("stationName"), if (stopPointDepartureObject.has("destinationName")) stopPointDepartureObject.getString("destinationName") else "Undefined", stopPointDepartureObject.getString("lineName"), stopPointDepartureObject.getString("towards"), stopPointDepartureObject.getString("expectedArrival")))
            }
        } catch (e: Exception) {
            exception = e
        }

        return listofDepartures
    }

    override fun onPostExecute(result: ArrayList<TubeDeparture>) {
        if (exception != null) {
            delegate!!.error(exception!!)
        } else {
            delegate!!.response(result)
        }
    }
}