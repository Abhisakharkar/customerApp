package com.example.chinmay.anew.localDatabase;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.chinmay.anew.model.ProductDetail;

@Database(entities = {ProductDetail.class},version = 1,exportSchema = false)
@TypeConverters(TimeStampCoverter.class)
public abstract class ProductDatabase extends RoomDatabase{

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "PRODUCT_DATABASE";
    private static ProductDatabase productDatabase;

    public static ProductDatabase getShowDatabase(Context context){
        if(productDatabase == null){
            synchronized (LOCK){
                productDatabase = Room.databaseBuilder(context.getApplicationContext(),
                        ProductDatabase.class,
                        ProductDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return productDatabase;
    }

    public abstract ProductDao productDao();
}
