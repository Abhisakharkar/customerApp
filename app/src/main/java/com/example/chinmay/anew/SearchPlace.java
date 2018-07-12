package com.example.chinmay.anew;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchPlace extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    private int countofkeys;
    private ArrayList<String> locations;
    private RecyclerView SavedPlacesRview;
    private LinearLayoutManager mLayoutManager;
    private MyAdapterRecyclerSavedPlaces madapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchplace);
        locations=new ArrayList<>();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        AutoCompleteTextView autocompleteView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        autocompleteView.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.autocomplete));
        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get data associated with the specified position
                // in the list (AdapterView)
                String description = (String) parent.getItemAtPosition(position);
                Toast.makeText(SearchPlace.this, description, Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                countofkeys = sharedpreferences.getInt("count", 0);
                countofkeys++;
                editor.putString("key"+countofkeys,description );
                editor.putInt("count",countofkeys );
                locations.add(description);
                SavedPlacesRview.setAdapter(madapter);
                editor.apply();

            }
        });

        SharedPreferences.Editor editor = sharedpreferences.edit();
        countofkeys = sharedpreferences.getInt("count", 0);
        if(countofkeys>0)
        {
            for (int i=1;i<=countofkeys;i++)
            {     locations.add(sharedpreferences.getString("key"+i,"Not Available"));}
        }
        SavedPlacesRview = (RecyclerView) findViewById(R.id.my_recycler_view);
        SavedPlacesRview.setHasFixedSize(true);
        SavedPlacesRview.setItemViewCacheSize(20);
        SavedPlacesRview.setDrawingCacheEnabled(true);
        SavedPlacesRview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mLayoutManager = new LinearLayoutManager(this);
        SavedPlacesRview.setLayoutManager(mLayoutManager);
        SavedPlacesRview.addItemDecoration(new VerticalSpaceItemDecoration(0));
        SavedPlacesRview.addItemDecoration(new DividerItemDecoration(SearchPlace.this, R.drawable.divider));
        madapter = new MyAdapterRecyclerSavedPlaces(this,locations);
        
        SavedPlacesRview.setAdapter(madapter);
        SavedPlacesRview.addOnItemTouchListener(
                new RecyclerItemClickListener(this, SavedPlacesRview ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );

    }
}
