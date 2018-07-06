package com.example.chinmay.anew;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private GpsTracker gpsTracker;
    String place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        Bundle b=getIntent().getExtras();

        if(b!=null)
        {
            place=b.getString("place");
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(place);

// Add the request to the RequestQueue.


    }
}
