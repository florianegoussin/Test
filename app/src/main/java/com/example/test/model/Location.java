package com.example.test.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

@Table(name = "Location")
public class Location extends Model {

    @Expose
    @Column(name = "id", index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String id;

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
    public LocationCoordinates coordinates;

    public LocationCoordinates getCoordinates(){
        if(coordinates == null){
            coordinates = new Select()
                    .from(LocationCoordinates.class)
                    .where("id='" + id.replace("'", "''") + "'")
                    .executeSingle();
        }
        return coordinates;
    }

    public Location(){
        super();
    }
}
