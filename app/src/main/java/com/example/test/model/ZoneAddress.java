package com.example.test.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

@Table(name = "PlaceAddress")
public class ZoneAddress extends Model {

    @Expose
    @Column(name = "name", index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String name;

    @Expose
    @Column(name = "city")
    public String city;

    @Expose
    @Column(name = "country")
    public String country;

    @Expose
    @Column(name = "location")
    public int location;

    @Expose
    @Column(name = "count")
    public int count;


    public ZoneAddress(){
        super();
    }


}
