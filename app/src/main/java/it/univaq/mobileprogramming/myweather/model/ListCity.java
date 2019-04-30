package it.univaq.mobileprogramming.myweather.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "cities")
public class ListCity{
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "icon")
    private int weatherIcon;
    @ColumnInfo(name = "temp")
    private String temp;
    @ColumnInfo(name = "condition")
    private String condition;
    @ColumnInfo(name = "code")
    private String code;

    public ListCity(String name, int weatherIcon, String temp, String condition, String code) {
        this.name = name;
        this.weatherIcon = weatherIcon;
        this.temp = temp;
        this.condition = condition;
        this.code = code;
    }

    @Ignore
    public ListCity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(int weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}


