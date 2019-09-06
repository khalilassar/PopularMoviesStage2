package com.example.popularmoviesstage2.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert
    public void insert(Movie movie);
    @Update
    public void update(Movie movie);
    @Delete
    public void delete(Movie movie);
    @Query("DELETE FROM Movie")
    public void deleteAll();
    @Query("SELECT * FROM Movie")
    LiveData<List<Movie>> getAllFavoriteLiveData();
    @Query("SELECT COUNT(id) FROM Movie where id=:id")
    int getFavoriteMovieCount(int id);
}
