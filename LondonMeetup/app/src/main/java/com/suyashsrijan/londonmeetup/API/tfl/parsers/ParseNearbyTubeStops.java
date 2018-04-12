package com.suyashsrijan.londonmeetup.API.tfl.parsers;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.suyashsrijan.londonmeetup.API.tfl.interfaces.ParseResultCallback;
import com.suyashsrijan.londonmeetup.API.tfl.models.Tube;
import com.suyashsrijan.londonmeetup.API.tfl.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParseNearbyTubeStops extends AsyncTask<JSONObject, Void, ArrayList<Tube>> {

    private ParseResultCallback<Tube> delegate = null;
    private Exception exception = null;

    public ParseNearbyTubeStops(ParseResultCallback<Tube> callback) {
        this.delegate = callback;
    }

    @Override
    protected ArrayList<Tube> doInBackground(JSONObject... params) {
        JSONObject stopPointData = params[0];
        ArrayList<Tube> listofStops = new ArrayList<>();
        try {
            JSONArray stopPointsArray = stopPointData.getJSONArray("stopPoints");
            for (int i = 0; i < stopPointsArray.length(); i++) {
                JSONObject stopPointObject = stopPointsArray.getJSONObject(i);
                listofStops.add(new Tube(Utils.getStoptypeEnum(stopPointObject.getString("stopType")), stopPointObject.getString("commonName"), stopPointObject.getString("id"), new LatLng(stopPointObject.getDouble("lat"), stopPointObject.getDouble("lon"))));
            }
        } catch (Exception e) {
            exception = e;
        }
        return listofStops;
    }
    @Override
    protected void onPostExecute(ArrayList<Tube> result){
        if (exception != null) {
            delegate.error(exception);
        } else{
            delegate.response(result);
        }
    }
}
