package it.univaq.mobileprogramming.myweather.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import it.univaq.mobileprogramming.myweather.R;
import it.univaq.mobileprogramming.myweather.model.Today;

public class ParsingToday {
    private Today today_object;

    public ParsingToday(String parsing) throws JSONException {
            JSONObject jor = new JSONObject(parsing);

            JSONObject main = jor.getJSONObject("main");
            JSONObject clouds = jor.getJSONObject("clouds");
            JSONArray  weather = jor.getJSONArray("weather");
            JSONObject sys = jor.getJSONObject("sys");
            JSONObject wind_object = jor.getJSONObject("wind");

            String wind = wind_object.getString("speed") + " km/h";

            Double temperatura= main.getDouble("temp");
                String temp = String.format ("%.1f", temperatura);
                if (temp.charAt(temp.length()-1) == '0') temp = temp.substring(0,temp.length()-2);
                temp = temp + "\u00B0";
            String pressione= main.getString("pressure") + " hPA";
            String umidita= main.getString("humidity") + " %";
            String min= main.getString("temp_min") + "\u00B0";
            String max= main.getString("temp_max") + "\u00B0";



            String country= sys.getString("country");
            int sunRise = sys.getInt("sunrise");
                String rise = sun_convert(sunRise);
            int sunSet = sys.getInt("sunset");
                String set = sun_convert(sunSet);

            JSONObject weather_object = weather.getJSONObject(0);
            String description = weather_object.getString("description");
            String condition;
                if (description.equals("poche nuvole")) condition = "Nuvoloso";
                else condition = description.toUpperCase().substring(0,1) + description.substring(1,description.length());
            String imm = weather_object.getString("icon");
            int image = setImm(imm);

            String cloud = clouds.getString("all") + " %";

            String city = jor.getString("name");

            String data = today_date();


            //creo l'oggetto today
            today_object = new Today(temp,city,country,condition,image,max,min,wind,pressione,umidita,cloud,rise,set,data);

        }

    public Today getToday_object() {
        return today_object;
    }

    public void setToday_object(Today today_object) {
        this.today_object = today_object;
    }

    private String today_date(){
            Date data = new  Date();
            Locale.setDefault(Locale.ITALIAN);
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM ");
            String formatted_date = sdf.format(data);
            return formatted_date;
        }

        private String sun_convert(int timestamp){
            String ora = new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date (timestamp*1000));
            return ora;
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
