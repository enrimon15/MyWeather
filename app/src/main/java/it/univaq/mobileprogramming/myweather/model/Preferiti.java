package it.univaq.mobileprogramming.myweather.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favourite")
public class Preferiti {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "nameCity")
    private String nameCity;
    @ColumnInfo(name = "countryCity")
    private String countryCity;

    @Ignore
    public Preferiti() {
    }

    public Preferiti(String nameCity, String countryCity) {
        this.nameCity = nameCity;
        this.countryCity = countryCity;
    }

    public String getNameCity() {
        return nameCity;
    }

    public void setNameCity(String nameCity) {
        this.nameCity = nameCity;
    }

    public String getCountryCity() {
        return countryCity;
    }

    public void setCountryCity(String countryCity) {
        this.countryCity = countryCity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
