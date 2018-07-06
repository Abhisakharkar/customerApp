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
    private Map<String, String> params;
     private String length;
    private String place;
    private JSONObject parameters;
    private RequestQueue requestQueue;
    private int flag;
    private serverops s1;

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

            s1=new serverops();
            s1.req(url,parameters,this);
            new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Splash.this,MainActivity.class);
                mainIntent.putExtra("place",serverops.place);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();


            }
        }, SPLASH_DISPLAY_LENGTH);


                                                          /* Create an Intent that will start the Menu-Activity. */



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




//

    }
}
