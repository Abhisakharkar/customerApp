package com.example.chinmay.anew.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.chinmay.anew.model.RetailerDetail;
import com.example.chinmay.anew.repository.ServerOperation;

public class RetailerViewModel extends ViewModel{

    private MutableLiveData<RetailerDetail> retailerDetailMutableLiveData;

    public LiveData<RetailerDetail> getRetailerDetail(Context context,String id){
        if(retailerDetailMutableLiveData == null){
            ServerOperation serverOperation = new ServerOperation(context);
            retailerDetailMutableLiveData = serverOperation.getRetailerDetail(id);
        }
        return retailerDetailMutableLiveData;
    }
}
