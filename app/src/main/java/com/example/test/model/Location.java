package com.example.test.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import java.io.Serializable;

@Table(name = "Location")
public class Location extends Model implements Serializable {

    @Expose
    @Column(name = "location", index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String location;

    @Expose
    @Column(name = "city")
    public String city;

    @Expose
    @Column(name = "country")
    public String country;

    @Expose
    @Column(name = "count")
    public int count;

    @Expose
    @Column(name = "coordinates")
    public LocationCoordinates coordinates;

    public LocationCoordinates getCoordinates(){
        if(coordinates == null){
            coordinates = new Select()
                    .from(LocationCoordinates.class)
                    .where("location='" + location.replace("'", "''") + "'")
                    .executeSingle();
        }
        return coordinates;
    }

   /* @Expose
    @Column(name = "measurements")
    public MesureSearchResult measurements;*/

    public Location(){
        super();
    }
}
