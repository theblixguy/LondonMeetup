package com.suyashsrijan.londonmeetup.API.tfl.parsers;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.suyashsrijan.londonmeetup.API.tfl.interfaces.ParseResultCallback;
import com.suyashsrijan.londonmeetup.API.tfl.models.Bike;
import com.suyashsrijan.londonmeetup.API.tfl.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParseNearbyBikePoints extends AsyncTask<JSONObject, Void, ArrayList<Bike>> {

    private ParseResultCallback<Bike> delegate = null;
    private Exception exception = null;

    public ParseNearbyBikePoints(ParseResultCallback<Bike> callback) {
        this.delegate = callback;
    }

    @Override
    protected ArrayList<Bike> doInBackground(JSONObject... params) {
        JSONObject bikePointData = params[0];
        ArrayList<Bike> listofPoints = new ArrayList<>();
        try {
            JSONArray stopPointsArray = bikePointData.getJSONArray("places");
            for (int i = 0; i < stopPointsArray.length(); i++) {
                JSONObject bikePointObject = stopPointsArray.getJSONObject(i);
                JSONArray bikePointAdditionalProps = bikePointObject.getJSONArray("additionalProperties");
                listofPoints.add(new Bike(Utils.getPlacetypeEnum(bikePointObject.getString("placeType")), bikePointObject.getString("commonName"), bikePointAdditionalProps.getJSONObject(6).getInt("value"), bikePointAdditionalProps.getJSONObject(7).getInt("value"), bikePointAdditionalProps.getJSONObject(8).getInt("value"), new LatLng(bikePointObject.getDouble("lat"), bikePointObject.getDouble("lon"))));
            }
        } catch (Exception e) {
            exception = e;
        }
        return listofPoints;
    }
    @Override
    protected void onPostExecute(ArrayList<Bike> result){
        if (exception != null) {
            delegate.error(exception);
        } else{
            delegate.response(result);
        }
    }
}
