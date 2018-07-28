package com.example.chinmay.anew.viewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.chinmay.anew.localDatabase.ProductDatabase;

public class ProductModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final ProductDatabase productDatabase;
    private final String Id;

    public ProductModelFactory(ProductDatabase productDatabase, String id) {
        this.productDatabase = productDatabase;
        this.Id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ProductDBViewModel(productDatabase,Id);
    }
}
