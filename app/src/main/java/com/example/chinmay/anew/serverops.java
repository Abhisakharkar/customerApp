package com.example.chinmay.anew;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class serverops {
    private String length;
    private RequestQueue requestQueue;
    private int flag=0;
    static String place;



    void req(String url, JSONObject parameters, final Context ctx) {

       requestQueue = Volley.newRequestQueue(ctx);

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
                                flag=1;
                            }
                            else {
                                JSONObject subLocality1Data = localityData.getJSONObject("subLocality1Data");
                                String tier1=subLocality1Data.getString("tier");
                                if(tier1.equals("0"))
                                {
                                    place= subLocality1Data.getString("subLocality1");
                                    flag=1;
                                }
                                else
                                {
                                    JSONObject subLocality2Data = localityData.getJSONObject("subLocality2Data");
                                    String tier2=subLocality2Data.getString("tier");
                                    if(tier2.equals("0"))
                                    {
                                        place= subLocality2Data.getString("subLocality2");
                                        flag=1;
                                    }
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ctx, "", Toast.LENGTH_SHORT).show();
                        Log.e("Volley", "Error");
                    }
                }
//
//            }
        );
        // Adds the JSON object request "obreq" to the request queue
        requestQueue.add(obreq);
//        new Handler().postDelayed(new Runnable(){
//            @Override
//            public void run() {
//                /* Create an Intent that will start the Menu-Activity. */
//
//            }
//        }, 2000);

    }


}
