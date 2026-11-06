package com.example.smartpantrymanager;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PantryDao {

    @Insert
    void insert(PantryItem item);

    @Update
    void update(PantryItem item);

    @Delete
    void delete(PantryItem item);

    @Query("SELECT * FROM pantry_items ORDER BY name ASC")
    LiveData<List<PantryItem>> getAllPantryItems();

    @Query("SELECT * FROM pantry_items")
    List<PantryItem> getAllItemsSync();
}
