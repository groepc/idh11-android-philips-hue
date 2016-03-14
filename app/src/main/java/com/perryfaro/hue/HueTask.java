package com.perryfaro.hue;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HueTask extends AsyncTask<String, Void, String> {

    // Static's
    private static final String urlString = "http://145.102.69.166:8080/api/newdeveloper";

    private String action;

    // Constructor, set listener
    public HueTask(String action) {
        System.out.println("Inner class");
        this.action = action;
    }

    @Override
    protected String doInBackground(String... params) {
        System.out.println("We komen hier");
        try {

            System.out.println("We komen hier 1");
            URL url = new URL(urlString + action);
            URLConnection urlConnection = url.openConnection();


            if (!(urlConnection instanceof HttpURLConnection)) {
                // Url
                System.out.println("We komen hier2");
                return null;
            }

            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setInstanceFollowRedirects(true);
            httpConnection.setRequestMethod("PUT");
            OutputStreamWriter out = new OutputStreamWriter(
                    httpConnection.getOutputStream());
            out.write("{\"on\":false, \"sat\":254, \"bri\":254,\"hue\":10000}");
            out.close();
            httpConnection.getInputStream();
            httpConnection.connect();


        } catch (MalformedURLException e) {
            System.out.println("Exception1");
            Log.e("TAG", e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            System.out.println("Exception2");
            Log.e("TAG", e.getLocalizedMessage());
            return null;
        }
        System.out.println("Voor return");
        return "";
    }


    protected void onPostExecute(String response) {


    }



}



