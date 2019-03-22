package it.univaq.mobileprogramming.myweather.model;

public class ListCity {
    private String name;
    private int weatherIcon;
    private String temp;
    private String condition;

    public ListCity(String name, int weatherIcon, String temp, String condition) {
        this.name = name;
        this.weatherIcon = weatherIcon;
        this.temp = temp;
        this.condition = condition;
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
}
