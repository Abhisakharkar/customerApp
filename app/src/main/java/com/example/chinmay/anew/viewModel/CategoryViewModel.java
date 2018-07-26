package com.example.chinmay.anew.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.chinmay.anew.model.Category;
import com.example.chinmay.anew.repository.ServerOperation;

import java.util.ArrayList;

public class CategoryViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Category>> arrayListMutableLiveData;

    public LiveData<ArrayList<Category>> getCategory(Context context){
        if(arrayListMutableLiveData == null){
            ServerOperation serverOperation = new ServerOperation(context);
            arrayListMutableLiveData = serverOperation.getCategories();
        }
        return arrayListMutableLiveData;
    }
}
