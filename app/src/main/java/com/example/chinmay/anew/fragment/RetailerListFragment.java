package com.example.chinmay.anew.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chinmay.anew.DividerItemDecoration;
import com.example.chinmay.anew.RetailerDetailAct;
import com.example.chinmay.anew.adapter.RetailersAdapter;
import com.example.chinmay.anew.R;
import com.example.chinmay.anew.RecyclerItemClickListener;
import com.example.chinmay.anew.model.RetailersList;
import com.example.chinmay.anew.repository.ServerOperation;
import com.example.chinmay.anew.VerticalSpaceItemDecoration;

import java.util.ArrayList;

/**
 * Created by Belal on 1/23/2018.
 */

public class RetailerListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private TextView retail;

    private LinearLayoutManager mLayoutManager;
    private RetailersAdapter madapter;
    private Handler handler;

    private ArrayList<RetailersList> retailersListArray;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.retailerlist, null);
        retail=(TextView)view.findViewById(R.id.retail);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.my_recycler_view);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        retailersListArray =new ArrayList<>();
        retailersListArray = ServerOperation.retailersArray;

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(0));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider));

        madapter = new RetailersAdapter(retailersListArray,getActivity());
        mRecyclerView.setAdapter(madapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), RetailerDetailAct.class);
                        if(retailersListArray != null && retailersListArray.size() > position){
                            intent.putExtra("RETID",retailersListArray.get(position).getRetailerId());
                        }
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }

                })
        );
        if(ServerOperation.retailersArray.size()==0) {
            retail.setText("No RetailersList found near you");
        }
    }

}