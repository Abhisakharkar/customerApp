package com.example.chinmay.anew;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Splash extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2500;
    private GpsTracker gpsTracker;
    double latitude,longitude;
    Map<String, String> params;
    String length;
    String place;
    JSONObject parameters;
    private RequestQueue requestQueue;
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        RequestQueue queue = Volley.newRequestQueue(this);
        gpsTracker = new GpsTracker(Splash.this);
        if(gpsTracker.canGetLocation())
        {
             latitude = gpsTracker.getLatitude();
             longitude = gpsTracker.getLongitude();
            params = new HashMap();
            params.put("latloc", ""+latitude);
            params.put("longloc", ""+longitude);
             parameters = new JSONObject(params);
             flag=1;

            Toast.makeText(this,"Location saved",Toast.LENGTH_LONG).show();

        }else{
            gpsTracker.showSettingsAlert();
        }
        String url ="http://ec2-18-222-137-50.us-east-2.compute.amazonaws.com:6868/get_location_ids";
        requestQueue = Volley.newRequestQueue(this);

// Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//
//                        // Display the first 500 characters of the response string.
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(Splash.this, "Please Check your connection", Toast.LENGTH_SHORT).show();
//            }
//        });
        if(flag==1) {
            JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.POST, url, parameters,
                    // The third parameter Listener overrides the method onResponse() and passes
                    //JSONObject as a parameter
                    new Response.Listener<JSONObject>() {

                        // Takes the response from the JSON request
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                 length = response.getString("length");


                            JSONObject localityData = response.getJSONObject("localityData");
                            String tier=localityData.getString("tier");
                            if(tier.equals("0"))
                            {
                                 place= localityData.getString("locality");
                            }
                            else {
                                JSONObject subLocality1Data = localityData.getJSONObject("subLocality1Data");
                                String tier1=subLocality1Data.getString("tier");
                                if(tier1.equals("0"))
                                {
                                     place= subLocality1Data.getString("subLocality1");
                                }
                                else
                                {
                                    JSONObject subLocality2Data = localityData.getJSONObject("subLocality2Data");
                                    String tier2=subLocality2Data.getString("tier");
                                    if(tier2.equals("0"))
                                    {
                                        place= subLocality2Data.getString("subLocality2");
                                    }
                                }
                            }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Intent mainIntent = new Intent(Splash.this,MainActivity.class);
                            mainIntent.putExtra("place",place);
                            Splash.this.startActivity(mainIntent);
                            Splash.this.finish();

                        }
                    },
                    // The final parameter overrides the method onErrorResponse() and passes VolleyError
                    //as a parameter
                    new Response.ErrorListener() {
                        @Override
                        // Handles errors that occur due to Volley
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Splash.this, "", Toast.LENGTH_SHORT).show();
                            Log.e("Volley", "Error");
                        }
                    }
//        @Override
//            public byte[] getBody()  {
//                HashMap<String, String> params2 = new HashMap<String, String>();
//                params2.put("latloc", ""+latitude);
//                params2.put("longloc", ""+longitude);
//                return new JSONObject(params2).toString().getBytes();
//            }
//
//            @Override
//            public String getBodyContentType() {
//                return "application/json";
//            }
            );
            // Adds the JSON object request "obreq" to the request queue
            requestQueue.add(obreq);
        }


//        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                Toast.makeText(Splash.this, "it worked", Toast.LENGTH_SHORT).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(Splash.this, "Please Check your connection", Toast.LENGTH_SHORT).show();
//            }
//        }) {
//
//        };
//        queue.add(sr);




//        new Handler().postDelayed(new Runnable(){
//            @Override
//            public void run() {
//                /* Create an Intent that will start the Menu-Activity. */
//
//            }
//        }, SPLASH_DISPLAY_LENGTH);

    }
}
