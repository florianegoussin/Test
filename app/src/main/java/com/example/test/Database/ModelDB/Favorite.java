package com.example.test.Database.ModelDB;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Favorite")
public class Favorite {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name="latitude")
    public double latitude;

    @ColumnInfo(name="longitude")
    public double longitude;

    @ColumnInfo(name="street")
    public String street;

    @ColumnInfo(name="zipCode")
    public String zipCode;

    @ColumnInfo(name="city")
    public String city;
}
