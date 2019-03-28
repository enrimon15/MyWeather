package it.univaq.mobileprogramming.myweather.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.univaq.mobileprogramming.myweather.R;
import it.univaq.mobileprogramming.myweather.model.ListCity;

public class ParsingFavourite {
    private ListCity city;

    public ParsingFavourite(String parsing) throws JSONException {
        JSONObject jor = new JSONObject(parsing);

        JSONObject main = jor.getJSONObject("main");
        JSONArray weather = jor.getJSONArray("weather");
        JSONObject sys = jor.getJSONObject("sys");

        String id = jor.getString("id");

        int temperatura = main.getInt("temp");
        String temp = temperatura + "\u00B0";

        String country = sys.getString("country");

        JSONObject weather_object = weather.getJSONObject(0);
        String description = weather_object.getString("description");

        String imm = weather_object.getString("icon");
        int image = setImm(imm);

        String cityname = jor.getString("name");

        city = new ListCity(cityname + ", " + country, image, temp, description, id);
    }

    public ListCity getCity() {
        return city;
    }

    public void setCity(ListCity city) {
        this.city = city;
    }

    private int setImm(String imm) {
        int image = 0;
        if (imm.equals("01d")) image = R.drawable.sun;
        else if (imm.equals("01n")) image = R.drawable.sun;
        else if (imm.equals("02d")) image = R.drawable.cloudy;
        else if (imm.equals("02n")) image = R.drawable.cloudy_night;
        else if (imm.equals("03d")) image = R.drawable.cloud;
        else if (imm.equals("03n")) image = R.drawable.cloud;
        else if (imm.equals("04d")) image = R.drawable.cloud;
        else if (imm.equals("04n")) image = R.drawable.cloud;
        else if (imm.equals("09d")) image = R.drawable.rain;
        else if (imm.equals("09n")) image = R.drawable.rain;
        else if (imm.equals("10d")) image = R.drawable.rain_sun;
        else if (imm.equals("10n")) image = R.drawable.rain_night;
        else if (imm.equals("11d")) image = R.drawable.lightning;
        else if (imm.equals("11n")) image = R.drawable.lightning_night;
        else if (imm.equals("13d")) image = R.drawable.snowing;
        else if (imm.equals("13n")) image = R.drawable.snowing_night;
        else if (imm.equals("50d")) image = R.drawable.windy;
        else if (imm.equals("50n")) image = R.drawable.windy;
        return image;
    }
}
