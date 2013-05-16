package com.mando.helper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;


import android.os.AsyncTask;
import android.util.Log;

import com.mando.R;

public class LocationHelper {
    public static void getLocationName(String lat, String lng, final Callback cb) {
        // 0: Twitter token
        // 1: Twitter message
        AsyncTask<String, Void, Void> x = new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {
                String lat = params[0];
                String lng = params[1];
                
                try { 
                    String uri = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&sensor=false";
                    Log.e("mando", "URI: " + uri);
                    URL url = new URL(uri); 
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
                    connection.setDoOutput(true); 
                    connection.setInstanceFollowRedirects(false); 
                    connection.setRequestMethod("GET"); 
                    connection.setRequestProperty("Content-Type", "application/xml"); 
                    connection.setUseCaches (false);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    OutputStream os = connection.getOutputStream(); 
                    InputStream is = connection.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuffer response = new StringBuffer(); 
                    while((line = rd.readLine()) != null) {
                      response.append(line);
                      response.append('\r');
                    }
                    rd.close();

                    connection.getResponseCode(); 
                    if (connection.getResponseCode() != 200) return null;
                    connection.disconnect();
                    
                    JSONObject json = new JSONObject(response.toString());
                    
                    if (json.getString("status").equals("ZERO_RESULTS"))
                        cb.onFailure();
                    else {
                        String hasil = json.getJSONArray("results").getJSONObject(0).getString("formatted_address");
                        cb.onSuccess(hasil);
                    }
                        
                } catch(Exception e) { 
                    e.printStackTrace();
                    cb.onFailure();
                }
                return null;
            }

        };
        x.execute(lat, lng);
    }
}