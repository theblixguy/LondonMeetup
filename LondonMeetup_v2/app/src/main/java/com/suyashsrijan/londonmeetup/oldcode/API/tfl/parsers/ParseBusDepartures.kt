package com.suyashsrijan.londonmeetup.API.tfl.parsers

import android.os.AsyncTask

import com.suyashsrijan.londonmeetup.API.tfl.interfaces.ParseResultCallback
import com.suyashsrijan.londonmeetup.newcode.app.network.model.bus.BusDeparture

import org.json.JSONArray

import java.util.ArrayList

class ParseBusDepartures(callback: ParseResultCallback<BusDeparture>) : AsyncTask<JSONArray, Void, ArrayList<BusDeparture>>() {

    private var delegate: ParseResultCallback<BusDeparture>? = null
    private var exception: Exception? = null

    init {
        this.delegate = callback
    }

    override fun doInBackground(vararg params: JSONArray): ArrayList<BusDeparture> {
        val stopPointsDepartureArray = params[0]
        val listofDepartures = ArrayList<BusDeparture>()
        try {
            for (i in 0 until stopPointsDepartureArray.length()) {
                val stopPointDepartureObject = stopPointsDepartureArray.getJSONObject(i)
                listofDepartures.add(BusDeparture(stopPointDepartureObject.getString("stationName"), if (stopPointDepartureObject.has("destinationName")) stopPointDepartureObject.getString("destinationName") else "Undefined", stopPointDepartureObject.getString("lineName"), stopPointDepartureObject.getString("towards"), stopPointDepartureObject.getString("expectedArrival")))
            }
        } catch (e: Exception) {
            exception = e
        }

        return listofDepartures
    }

    override fun onPostExecute(result: ArrayList<BusDeparture>) {
        if (exception != null) {
            delegate!!.error(exception!!)
        } else {
            delegate!!.response(result)
        }
    }
}