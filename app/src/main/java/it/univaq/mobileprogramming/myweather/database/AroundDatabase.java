package it.univaq.mobileprogramming.myweather.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import it.univaq.mobileprogramming.myweather.model.ListCity;

@Database(entities ={ ListCity.class }, version = 1, exportSchema = false)
public abstract class AroundDatabase extends RoomDatabase {

    public abstract AroundDAO getAroundDAO();

    private static AroundDatabase instance = null;

    public AroundDatabase(){}

    public static AroundDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(
                    context,
                    AroundDatabase.class,
                    "aroundDatabase").build();
        }
        return instance;
    }
}
