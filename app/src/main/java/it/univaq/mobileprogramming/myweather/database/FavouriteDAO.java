package it.univaq.mobileprogramming.myweather.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import it.univaq.mobileprogramming.myweather.model.Preferiti;

@Dao
public interface FavouriteDAO {

    @Query("SELECT * FROM favourite")
    List<Preferiti> getAll();

    @Insert
    public void save(Preferiti city);

    @Delete
    public void delete(Preferiti city);

    @Query("DELETE FROM favourite")
    public void deleteAll();
}
