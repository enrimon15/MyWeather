package it.univaq.mobileprogramming.myweather.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.univaq.mobileprogramming.myweather.R;
import it.univaq.mobileprogramming.myweather.model.ListCity;

public class ParsingAround {
    private List<ListCity> around = new ArrayList<ListCity>();

    public ParsingAround(String parsing) throws JSONException {
        JSONObject jor = new JSONObject(parsing);
        JSONArray lista = jor.getJSONArray("list");

        for (int i = 0; i < lista.length(); i++) {
            JSONObject oggetto = lista.getJSONObject(i);
            String nome = oggetto.getString("name");

            JSONObject main = oggetto.getJSONObject("main");
            int temperatura = main.getInt("temp");
            String temp = temperatura + "\u00B0";

            JSONArray weather = oggetto.getJSONArray("weather");
            JSONObject icon = weather.getJSONObject(0);
            String imm = icon.getString("icon");
            int immagine = setImm(imm);

            String description = icon.getString("description");
            String condition = description.toUpperCase().substring(0,1) + description.substring(1,description.length());

            String id = oggetto.getString("id");

            around.add(new ListCity(nome,immagine,temp,condition, id));
        }

        for (int x = 0; x<around.size()-1; x++){
            String n = (around.get(x)).getName();
            if (n.equals((around.get(x+1)).getName())) around.remove(x);
            if (n.charAt(1) == '\'') around.remove(x+1);

        }
    }

    public List<ListCity> getAround() {
        return around;
    }

    public void setAround(List<ListCity> around) {
        this.around = around;
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
