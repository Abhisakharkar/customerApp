package com.example.chinmay.anew;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tooltip.Tooltip;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<retailers> retailersArray;
    private GpsTracker gpsTracker;
    private String place;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    android.support.v7.app.AlertDialog d;
    private MyAdapter madapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        retailersArray=new ArrayList<>();
        Bundle b=getIntent().getExtras();

        if(b!=null)
        {
            place=b.getString("place");

        }

        ActionBar actionBar = getSupportActionBar();
        if(place==null)
        {
            actionBar.setTitle("Not Available");
        }
        else
        {
            actionBar.setTitle(place);
        }


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mLayoutManager = new LinearLayoutManager(this);
        //mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(0));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, R.drawable.divider));
        madapter = new MyAdapter(this);
        showTooltip(R.id.my_toolbar, Gravity.BOTTOM);
        mRecyclerView.setAdapter(madapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }

                })
        );

    }

    private void showTooltip(int my_toolbar, int top)
    {
        Tooltip tooltip=new Tooltip.Builder(toolbar)
                .setText("If you aren't located right, please select your location here")
                .setTextColor(Color.WHITE)
                .setGravity(top)
                .setDismissOnClick(true)
                .setCancelable(true)
                .setBackgroundColor(Color.parseColor("#648ab4"))
                .setTextSize(20f).show();
    }
}





















//        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
//
//        // Get the layout inflater
//        LayoutInflater inflater = this.getLayoutInflater();
//        final View prompt = inflater.inflate(R.layout.dialog_pay, null);
//
//
//        // Inflate and set the layout for the dialog
//        // Pass null as the parent view because its going in the dialog layout
//        builder.setView(prompt);
//        Button yes=(Button)prompt.findViewById(R.id.yes);
//        Button change=(Button)prompt.findViewById(R.id.change);
//
//        yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "YES", Toast.LENGTH_SHORT).show();
//                d.cancel();
//
//            }
//        });
//        change.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Change", Toast.LENGTH_SHORT).show();
//                d.cancel();
//            }
//        });
//
//         d=builder.create();
//
//        d.show();
