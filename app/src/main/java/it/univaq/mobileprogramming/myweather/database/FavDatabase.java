package it.univaq.mobileprogramming.myweather.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import it.univaq.mobileprogramming.myweather.model.Preferiti;

@Database(entities ={ Preferiti.class }, version = 2, exportSchema = false)
public abstract class FavDatabase extends RoomDatabase {
    public abstract FavouriteDAO favouriteDAO();
}
