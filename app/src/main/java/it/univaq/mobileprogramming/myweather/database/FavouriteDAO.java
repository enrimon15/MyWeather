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

    @Query("SELECT * FROM favourite WHERE (nameCity=:name and countryCity=:country)")
    public Preferiti getFav(String name, String country);

    @Query("SELECT * FROM favourite WHERE id=:idx")
    public Preferiti getFavv(long idx);

    @Insert
    public void save(Preferiti city);

    @Query("DELETE FROM favourite WHERE codeCity=:cod")
    public void delete(String cod);

    @Query("DELETE FROM favourite")
    public void deleteAll();
}
