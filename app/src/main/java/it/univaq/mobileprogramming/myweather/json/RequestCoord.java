package it.univaq.mobileprogramming.myweather.json;

public class RequestCoord {
    private String url = "https://api.openweathermap.org/data/2.5/weather?";
    private String lat;
    private String lon;

    public RequestCoord(String lat, String lon, String key, String unit) {
        this.lat = lat;
        this.lon = lon;
        this.url = url + "lat=" + lat + "&lon=" + lon + "&appid=" + key + unit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
