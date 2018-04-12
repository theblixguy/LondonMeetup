package com.suyashsrijan.londonmeetup.API.tfl.parsers;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.suyashsrijan.londonmeetup.API.tfl.interfaces.ParseResultCallback;
import com.suyashsrijan.londonmeetup.API.tfl.models.Bus;
import com.suyashsrijan.londonmeetup.API.tfl.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParseNearbyBusStops extends AsyncTask<JSONObject, Void, ArrayList<Bus>> {

    private ParseResultCallback<Bus> delegate = null;
    private Exception exception = null;

    public ParseNearbyBusStops(ParseResultCallback<Bus> callback) {
        this.delegate = callback;
    }

    @Override
    protected ArrayList<Bus> doInBackground(JSONObject... params) {
        JSONObject stopPointData = params[0];
        ArrayList<Bus> listofStops = new ArrayList<>();
        try {
            JSONArray stopPointsArray = stopPointData.getJSONArray("stopPoints");
            for (int i = 0; i < stopPointsArray.length(); i++) {
                JSONObject stopPointObject = stopPointsArray.getJSONObject(i);
                listofStops.add(new Bus(Utils.getStoptypeEnum(stopPointObject.getString("stopType")), stopPointObject.getString("commonName"), stopPointObject.getString("indicator"), stopPointObject.getString("id"), new LatLng(stopPointObject.getDouble("lat"), stopPointObject.getDouble("lon"))));
            }
        } catch (Exception e) {
            exception = e;
        }
        return listofStops;
    }
    @Override
    protected void onPostExecute(ArrayList<Bus> result){
        if (exception != null) {
            delegate.error(exception);
        } else{
            delegate.response(result);
        }
    }
}
