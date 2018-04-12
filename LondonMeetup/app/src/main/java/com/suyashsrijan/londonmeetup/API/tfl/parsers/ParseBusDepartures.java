package com.suyashsrijan.londonmeetup.API.tfl.parsers;

import android.os.AsyncTask;

import com.suyashsrijan.londonmeetup.API.tfl.interfaces.ParseResultCallback;
import com.suyashsrijan.londonmeetup.API.tfl.models.BusDeparture;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParseBusDepartures extends AsyncTask<JSONArray, Void, ArrayList<BusDeparture>> {

    private ParseResultCallback<BusDeparture> delegate = null;
    private Exception exception = null;

    public ParseBusDepartures(ParseResultCallback<BusDeparture> callback) {
        this.delegate = callback;
    }

    @Override
    protected ArrayList<BusDeparture> doInBackground(JSONArray... params) {
        JSONArray stopPointsDepartureArray = params[0];
        ArrayList<BusDeparture> listofDepartures = new ArrayList<>();
        try {
            for (int i = 0; i < stopPointsDepartureArray.length(); i++) {
                JSONObject stopPointDepartureObject = stopPointsDepartureArray.getJSONObject(i);
                listofDepartures.add(new BusDeparture(stopPointDepartureObject.getString("stationName"), (stopPointDepartureObject.has("destinationName") ? stopPointDepartureObject.getString("destinationName") : "Undefined"), stopPointDepartureObject.getString("lineName"), stopPointDepartureObject.getString("towards"), stopPointDepartureObject.getString("expectedArrival")));
            }
        } catch (Exception e) {
            exception = e;
        }
        return listofDepartures;
    }

    @Override
    protected void onPostExecute(ArrayList<BusDeparture> result) {
        if (exception != null) {
            delegate.error(exception);
        } else {
            delegate.response(result);
        }
    }
}