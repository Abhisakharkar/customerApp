package com.example.chinmay.anew.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.chinmay.anew.model.ProductDetail;
import com.example.chinmay.anew.repository.ServerOperation;

import java.util.ArrayList;

public class ProductDetailViewModel extends ViewModel {

    private MutableLiveData<ArrayList<ProductDetail>> arrayListMutableLiveData;

    public LiveData<ArrayList<ProductDetail>> getProductDetail(Context context,String id){
        if(arrayListMutableLiveData == null){
            ServerOperation serverOperation = new ServerOperation(context);
            arrayListMutableLiveData = serverOperation.getProductDetails(id);
        }
        return arrayListMutableLiveData;
    }
}
