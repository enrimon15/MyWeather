package it.univaq.mobileprogramming.myweather.json;

public class RequestFiveDays {
    private String url = "https://api.openweathermap.org/data/2.5/forecast?q=";
    private String city;
    private String country;

    public RequestFiveDays(String city, String country, String key, String unit) {
        this.city = city;
        this.country = country;
        this.url = url + city + "," + country + "&appid=" + key + unit;
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

    public String getUrl() {
        return url;
    }
}
