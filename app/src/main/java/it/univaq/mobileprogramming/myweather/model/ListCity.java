package it.univaq.mobileprogramming.myweather.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

//@Entity(tableName = "cities")
public class ListCity{
    //@PrimaryKey(autoGenerate = true)
    private long id;
    //@ColumnInfo(name = "name")
    private String name;
    //@ColumnInfo(name = "icon")
    private int weatherIcon;
    //@ColumnInfo(name = "temp")
    private String temp;
    //@ColumnInfo(name = "condition")
    private String condition;
    //@ColumnInfo(name = "code")
    private String code;

    public ListCity(String name, int weatherIcon, String temp, String condition, String code) {
        this.name = name;
        this.weatherIcon = weatherIcon;
        this.temp = temp;
        this.condition = condition;
        this.code = code;
    }

    //@Ignore
    public ListCity() {
    }

    public String getNameCity() {
        return name;
    }

    public void setNameCity(String name) {
        this.name = name;
    }

    public int getWeatherIconCity() {
        return weatherIcon;
    }

    public void setWeatherIconCity(int weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public String getTempCity() {
        return temp;
    }

    public void setTempCity(String temp) {
        this.temp = temp;
    }

    public String getConditionCity() {
        return condition;
    }

    public void setConditionCity(String condition) {
        this.condition = condition;
    }

    public String getcode() {
        return code;
    }

    public void setcode(String code) {
        this.code = code;
    }
}


