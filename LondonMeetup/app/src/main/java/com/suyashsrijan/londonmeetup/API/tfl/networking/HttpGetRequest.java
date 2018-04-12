package com.suyashsrijan.londonmeetup.API.tfl.networking;

import android.os.AsyncTask;

import com.suyashsrijan.londonmeetup.API.tfl.interfaces.HttpGetResultCallback;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpGetRequest extends AsyncTask<String, Void, String> {

    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;

    private HttpGetResultCallback delegate = null;
    private Exception exception = null;

    public HttpGetRequest(HttpGetResultCallback callback) {
        this.delegate = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        String urlString = params[0];
        String inputLine;
        String result = "";
        try {
            URL myUrl = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.connect();

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }
            reader.close();
            streamReader.close();
            result = stringBuilder.toString();
            return result;
        } catch (Exception e) {
            exception = e;
        }
        return result;
    }
    @Override
    protected void onPostExecute(String result){
        if (exception != null) {
            delegate.error(exception);
        } else{
            delegate.response(result);
        }
    }
}