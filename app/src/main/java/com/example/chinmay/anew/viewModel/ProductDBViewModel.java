package com.example.chinmay.anew.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.chinmay.anew.localDatabase.ProductDatabase;
import com.example.chinmay.anew.model.ProductDetail;

public class ProductDBViewModel extends ViewModel {

    private LiveData<ProductDetail> productDetailLiveData;

    public ProductDBViewModel(ProductDatabase productDatabase,String id) {
        productDetailLiveData = productDatabase.productDao().getProductDetailFromDb(id);
    }

    public LiveData<ProductDetail> getProductDetailLiveData() {
        return productDetailLiveData;
    }
}
