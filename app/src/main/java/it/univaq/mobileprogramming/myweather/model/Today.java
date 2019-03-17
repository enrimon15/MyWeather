package it.univaq.mobileprogramming.myweather.model;

public class Today {
    private String temp;
    private String city;
    private String country;
    private String weatherResult;
    private String date;

    //details
    private String max;
    private String min;
    private String wind;
    private String pressure;
    private String humidity;
    private String clouds;
    private String sunrise;
    private String sunset;
    private int icon;

    public Today(String temp, String city, String country, String weatherResult, int icon, String max, String min, String wind, String pressure, String humidity, String clouds, String sunrise, String sunset, String data) {
        this.temp = temp;
        this.city = city;
        this.country = country;
        this.weatherResult = weatherResult;
        this.icon = icon;
        this.max = max;
        this.min = min;
        this.wind = wind;
        this.pressure = pressure;
        this.humidity = humidity;
        this.clouds = clouds;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.date = data;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWeatherResult() {
        return weatherResult;
    }

    public void setWeatherResult(String weatherResult) {
        this.weatherResult = weatherResult;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getClouds() {
        return clouds;
    }

    public void setClouds(String clouds) {
        this.clouds = clouds;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}