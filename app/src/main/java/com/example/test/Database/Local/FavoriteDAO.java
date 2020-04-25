package com.example.test.Database.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.test.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;



@Dao
public interface FavoriteDAO {

    @Query("SELECT * FROM Favorite")
    Flowable<List<Favorite>> getFavItems();

    @Query("SELECT EXISTS (SELECT 1 FROM Favorite WHERE location=:itemId)")
    int isFavorite(String itemId);

    @Insert
    void insertFav(Favorite...favorites);

    @Delete
    void delete(Favorite favorite);

}