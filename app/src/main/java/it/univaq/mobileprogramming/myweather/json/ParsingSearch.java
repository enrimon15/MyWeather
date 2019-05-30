package it.univaq.mobileprogramming.myweather.json;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import it.univaq.mobileprogramming.myweather.model.CitySearch;

public class ParsingSearch {
    List<CitySearch> cityList = new ArrayList<>();
    private String CITIES_FILE_NAME = "city_list.json";
    Context cont;
    private static ParsingSearch instance = null;

    public static synchronized ParsingSearch getInstance(Context context){
        if (instance == null) instance = new ParsingSearch(context);
        return instance;
    }

    private ParsingSearch(Context context){
        cont = context;
    }

    public void loadJson(){

        try {
            InputStream is = cont.getAssets().open(CITIES_FILE_NAME);
            JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));

            reader.beginArray();

            Gson gson = new GsonBuilder().create();

            while (reader.hasNext()) {

                CitySearch cityJson = gson.fromJson(reader, CitySearch.class);
                cityList.add(cityJson);

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public List<CitySearch> getList () {
        return cityList;
    }
}
