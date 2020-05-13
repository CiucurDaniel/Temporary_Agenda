package com.example.temoporaryagenda;

import androidx.room.TypeConverter;

import java.util.Date;

/*
Utility class use for TypeConverting
between Date type and Long

Android ROOM doesn't know date type by default
and this is why we have to define such a class
 */
public class Converter {
    @TypeConverter
    public static Date fromDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToLong(Date date) {
        return date == null ? null : date.getTime();
    }
}
