package com.example.chinmay.anew;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class SearchPlace extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    private int countOfKeys;
    private ArrayList<String> locations;
    private RecyclerView savedPlacesRecyView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapterRecyclerSavedPlaces madapter;
    private PlacesAutoCompleteAdapter PlacesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchplace);
        locations=new ArrayList<>();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        PlacesAdapter=new PlacesAutoCompleteAdapter(this, R.layout.autocomplete);
        AutoCompleteTextView autocompleteView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        autocompleteView.setAdapter(PlacesAdapter);
        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get data associated with the specified position
                // in the list (AdapterView)
                String description = (String) parent.getItemAtPosition(position);

                Toast.makeText(SearchPlace.this, description, Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                countOfKeys = sharedpreferences.getInt("count", 0);
                MyGooglePlaces myplace= PlacesAdapter.getSelectedItem1(position);
                countOfKeys++;
                editor.putString("key"+countOfKeys,description );
                editor.putInt("count",countOfKeys );    //Adding the search results in the RecyclerView/SHared preference
                locations.add(description);
                Double lat=myplace.getLatitude();
                Double longi=myplace.getLongitude();
                savedPlacesRecyView.setAdapter(madapter);
                editor.apply();
                Intent i =new Intent(SearchPlace.this,MapActivity2.class);
                i.putExtra("lat",lat);
                i.putExtra("long",longi);

                startActivityForResult(i,3);


            }
        });


        countOfKeys = sharedpreferences.getInt("count", 0);
        if(countOfKeys>0)
        {
            for (int i=1;i<=countOfKeys;i++)
            {     locations.add(sharedpreferences.getString("key"+i,"Not Available"));}
        }
        savedPlacesRecyView = (RecyclerView) findViewById(R.id.my_recycler_view);
        savedPlacesRecyView.setHasFixedSize(true);
        savedPlacesRecyView.setItemViewCacheSize(20);
        savedPlacesRecyView.setDrawingCacheEnabled(true);
        savedPlacesRecyView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mLayoutManager = new LinearLayoutManager(this);
        savedPlacesRecyView.setLayoutManager(mLayoutManager);
        savedPlacesRecyView.addItemDecoration(new VerticalSpaceItemDecoration(0));
        savedPlacesRecyView.addItemDecoration(new DividerItemDecoration(SearchPlace.this, R.drawable.divider));
        madapter = new MyAdapterRecyclerSavedPlaces(this,locations);
        savedPlacesRecyView.setAdapter(madapter);
        savedPlacesRecyView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, savedPlacesRecyView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        {
            if(requestCode==3)
            {
                setResult(3,data);
                finish();
            }
        }
    }
}
