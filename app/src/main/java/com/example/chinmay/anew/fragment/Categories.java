package com.example.chinmay.anew.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chinmay.anew.R;
import com.example.chinmay.anew.RecyclerItemClickListener;
import com.example.chinmay.anew.adapter.CategoriesAdapter;
import com.example.chinmay.anew.model.Category;
import com.example.chinmay.anew.repository.ServerOperation;
import com.example.chinmay.anew.view.ChildView;
import com.example.chinmay.anew.view.HeaderView;
import com.mindorks.placeholderview.ExpandablePlaceHolderView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Categories extends Fragment {

    private RecyclerView recyclerView1;
    private ExpandablePlaceHolderView recyclerView2;

    private ArrayList<Category> categories1 = new ArrayList<>();
    private ArrayList<Category> categories = new ArrayList<>();

    private Map<String,List<Category>> categoryMap;

    public Categories() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        recyclerView1 = view.findViewById(R.id.recylcerViewMainCat);
        recyclerView2 = view.findViewById(R.id.recylcerViewSecCat);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        findItemAtLvl();
        recyclerView1.setHasFixedSize(true);
        recyclerView2.setHasFixedSize(true);

        recyclerView1.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView1,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        findChildAt1(position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }

                })
        );

        recyclerView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Toast.makeText(getContext(),"Clicked", view.getId()).show();
            }
        });

    }

    private void findItemAtLvl(){
        final ServerOperation serverOperation = new ServerOperation(getActivity());
        serverOperation.getCategories();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                categories = serverOperation.getCategoriesList();
                for(int i = 0; i< categories.size(); i++){
                    try{
                        if(Integer.parseInt(categories.get(i).getLevel()) == 0){
                            categories1.add(categories.get(i));
                        }
                    }catch (Exception e){

                    }
                }
                CategoriesAdapter myAdapter = new CategoriesAdapter(categories1,getActivity());
                recyclerView1.setAdapter(myAdapter);
                findChildAt1(0);
            }
        },1000);
    }

    private void findChildAt1(int pos){
        ArrayList<Category> arrayList = new ArrayList<>();
        ArrayList<Integer> childId = categories.get(pos).getChildren();
        try{
            for(int i=0;i<childId.size();i++){
                for(int j=0;j<categories.size();j++){
                    try{
                        if(Integer.parseInt(categories.get(j).getId()) == childId.get(i)){
                            arrayList.add(categories.get(j));
                        }
                    }catch (Exception e){

                    }
                }
            }

            for(int i=0;i<arrayList.size();i++){
                recyclerView2.addView(new HeaderView(getActivity(), arrayList.get(i).getName(),arrayList.get(i).getImageUrl()));
                findChildAt2(i,arrayList);
            }

        }catch (Exception e){

        }
    }

    private void findChildAt2(int pos,ArrayList<Category> categoryArrayList){
        ArrayList<Category> arrayList = new ArrayList<>();
        ArrayList<Integer> childId = categoryArrayList.get(pos).getChildren();
        try{
            for(int i=0;i<childId.size();i++){
                for(int j=0;j<categories.size();j++){
                    try{
                        if(Integer.parseInt(categories.get(j).getId()) == childId.get(i)){
                            arrayList.add(categories.get(j));
                        }
                    }catch (Exception e){

                    }
                }
            }
            for(int i=0;i<arrayList.size();i++){
                recyclerView2.addView(new ChildView(getActivity(),arrayList.get(i)));
            }

        }catch (Exception e){

        }
    }

}