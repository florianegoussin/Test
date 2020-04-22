package com.example.test.Database.ModelDB;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Favorite")
public class Favorite {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name="location")
    public String location;

    @ColumnInfo(name="city")
    public String city;

    @ColumnInfo(name="country")
    public String country;

    //@ColumnInfo(name="coordinates")
    // public LocationCoordinates coordinates;


}