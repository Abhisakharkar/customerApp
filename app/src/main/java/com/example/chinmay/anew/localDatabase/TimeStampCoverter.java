package com.example.chinmay.anew.localDatabase;

import android.arch.persistence.room.TypeConverter;

import java.sql.Timestamp;
import java.util.Date;

public class TimeStampCoverter {

    @TypeConverter
    public static Date toDate(Long timeStamp){
        return timeStamp == null? null : new Date(timeStamp);
    }

    @TypeConverter
    public static Long toTimeStamp(Date date){
        return date == null? null : date.getTime();
    }
}
