package it.univaq.mobileprogramming.myweather.json;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import it.univaq.mobileprogramming.myweather.model.Search.CitySearch;

public class ParsingSearch {
    List<CitySearch> cityList = new ArrayList<>();
    private final String CITIES_FILE_NAME = "city_list.json";
    Context cont;
    private static ParsingSearch instance = null;

    public static synchronized ParsingSearch getInstance(Context context){
        if (instance == null) instance = new ParsingSearch(context);
        return instance;
    }

    private ParsingSearch(Context context){
        cont = context;
    }

    /** parsing con jsonReader **/
    public void loadJson(){

        try {
            InputStream is = cont.getAssets().open(CITIES_FILE_NAME); //inputstream con il file contenente tutte le città
            JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8")); //creo il json reader dandogli in pasto l'inpiut stream

            reader.beginArray(); //inizio lettura array json

            Gson gson = new GsonBuilder().create();

            String checkDuplicate = ""; //il file gson fornito ha duplicati

            while (reader.hasNext()) { //fin quando l'array ha un oggetto non visto

                CitySearch cityJson = gson.fromJson(reader, CitySearch.class);
                if (!(checkDuplicate.equals(cityJson.getName()))) cityList.add(cityJson); //non inserisco i duplicati
                checkDuplicate = cityJson.getName();

            }
            reader.endArray(); //lettura terminata

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public List<CitySearch> getList () {
        return cityList;
    }
}
