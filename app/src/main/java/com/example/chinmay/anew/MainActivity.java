package com.example.chinmay.anew;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.chinmay.anew.fragment.AccountFragment;
import com.tooltip.Tooltip;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<retailers> retailersArray;
    private GpsTracker gpsTracker;
    private ActionBar actionBar;
    private String place;
    private int from_map=1;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    android.support.v7.app.AlertDialog d;
    private MyAdapterRecyclerMainAc madapter;
    private ProgressDialog prdialog;
    private ServerOp s1;
    private TextView retail;

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

        actionBar = getSupportActionBar();
        if(place==null)
        {
            actionBar.setTitle("Not Available");
        }
        else
        {
            actionBar.setTitle(place);
        }
        loadFragment(new RetailerListFragment());
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i=new Intent(MainActivity.this,SearchPlace.class);
                startActivityForResult(i,from_map);
            }
        });


//        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setItemViewCacheSize(20);
//        mRecyclerView.setDrawingCacheEnabled(true);
//        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
//        mLayoutManager = new LinearLayoutManager(this);
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(0));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, R.drawable.divider));
//        madapter = new MyAdapterRecyclerMainAc(this);
        showTooltip(R.id.my_toolbar, Gravity.BOTTOM);
//        mRecyclerView.setAdapter(madapter);
//        mRecyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(this, mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override public void onItemClick(View view, int position) {
//                        // do whatever
//
//                    }
//
//                    @Override
//                    public void onLongItemClick(View view, int position) {
//                    }
//
//                })
//        );

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==from_map)
        {
            prdialog = new ProgressDialog(this);
            prdialog.setTitle("Loading");
            prdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prdialog.setCancelable(false);
            prdialog.show();
            s1=new ServerOp(this);
            //Creating the object for server
            s1.requestServerLocationWithParameters(data.getDoubleExtra("latitude",79),data.getDoubleExtra("longitude",21));



            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String s=s1.getLocation();

                    if(s==null)
                    {
                        actionBar.setTitle("Not Available");
                    }
                    else {
                        actionBar.setTitle(s);
                    }
                    loadFragment(new RetailerListFragment());
                   // mRecyclerView.setAdapter(madapter);
                    prdialog.cancel();








                }

            },2500);

        }



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
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_nearme:

                        fragment = new RetailerListFragment();
                        break;




                case R.id.navigation_categories:
                    break;


                case R.id.navigation_cart:
                    break;


                case R.id.navigation_account:
                    fragment=new AccountFragment();
                    break;





            }
            return loadFragment(fragment);
        }

    };
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
