package it.univaq.mobileprogramming.myweather.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import it.univaq.mobileprogramming.myweather.model.ListCity;

@Dao
public interface AroundDAO {
    @Query("SELECT * FROM cities")
    List<ListCity> getAll();

    @Insert
    public void save(ListCity city);

    @Query("DELETE FROM cities")
    public void deleteAll();
}
