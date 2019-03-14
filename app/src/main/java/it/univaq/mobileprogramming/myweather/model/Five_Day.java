package it.univaq.mobileprogramming.myweather.model;

public class Five_Day{
    private String day;
    private int weatherIcon;
    private String max_temp;
    private String min_temp;

    public Five_Day(String day, String max_temp, String min_temp, int weatherIcon) {
        this.day = day;
        this.max_temp = max_temp;
        this.min_temp = min_temp;
        this.weatherIcon = weatherIcon;
    }

    public String getday() {
        return day;
    }

    public void setday(String day) {
        this.day = day;
    }

    public int getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(int weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public String getMax_temp() {
        return max_temp;
    }

    public void setMax_temp(String max_temp) {
        this.max_temp = max_temp;
    }

    public String getMin_temp() {
        return min_temp;
    }

    public void setMin_temp(String min_temp) {
        this.min_temp = min_temp;
    }
}
