package it.univaq.mobileprogramming.myweather.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import it.univaq.mobileprogramming.myweather.model.ListCity;
import it.univaq.mobileprogramming.myweather.model.Preferiti;

@Database(entities ={ Preferiti.class}, version = 2, exportSchema = false)
public abstract class FavDatabase extends RoomDatabase {
    public abstract FavouriteDAO favouriteDAO();

    private static FavDatabase instance = null;

    public FavDatabase(){}

    public static FavDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(
                    context,
                    FavDatabase.class,
                    "favouriteDB").allowMainThreadQueries().build();
        }
        return instance;
    }
}
