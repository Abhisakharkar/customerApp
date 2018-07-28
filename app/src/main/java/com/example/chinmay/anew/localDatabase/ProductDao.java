package com.example.chinmay.anew.localDatabase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.chinmay.anew.model.ProductDetail;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM ProductTable WHERE id = :productId")
    LiveData<ProductDetail> getProductDetailFromDb(String productId);

    @Insert
    void addProductDetailToDB(ProductDetail productDetail);

    @Update
    void updateProductDetail(ProductDetail productDetail);
}
