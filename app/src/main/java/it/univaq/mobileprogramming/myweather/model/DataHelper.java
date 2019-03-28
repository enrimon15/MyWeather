package it.univaq.mobileprogramming.myweather.model;

/**
 * Copyright (C) 2015 Ari C.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.widget.Filter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.label305.asynctask.SimpleAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import it.univaq.mobileprogramming.myweather.R;

public class DataHelper {

    private static final String CITIES_FILE_NAME = "city_list.json";

    private static List<String> lstCities;

    private static List<CitySearch> citySuggestions = new ArrayList<>();

    public interface OnFindColorsListener {
        void onResults(List<Today> results);
    }

    public interface OnFindSuggestionsListener {
        void onResults(List<CitySearch> results);
    }

    public static List<CitySearch> getHistory(Context context, int count) {

        List<CitySearch> suggestionList = new ArrayList<>();
        CitySearch colorSuggestion;
        for (int i = 0; i < citySuggestions.size(); i++) {
            colorSuggestion = citySuggestions.get(i);
            colorSuggestion.setIsHistory(true);
            suggestionList.add(colorSuggestion);
            if (suggestionList.size() == count) {
                break;
            }
        }
        return suggestionList;
    }

    public static void resetSuggestionsHistory() {
        for (CitySearch colorSuggestion : citySuggestions) {
            colorSuggestion.setIsHistory(false);
        }
    }

    public static void findSuggestions(final Context context, String query, final int limit, final long simulatedDelay,
                                       final OnFindSuggestionsListener listener) {
        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                try {
                    Thread.sleep(simulatedDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                DataHelper.resetSuggestionsHistory();
                initColorWrapperList(context);
                List<CitySearch> suggestionList = new ArrayList<>();
                if (!(constraint == null || constraint.length() == 0)) {

                    for (CitySearch suggestion : citySuggestions) {
                        if (suggestion.getName().toUpperCase()
                                .startsWith(constraint.toString().toUpperCase())) {

                            suggestionList.add(suggestion);
                            if (limit != -1 && suggestionList.size() == limit) {
                                break;
                            }
                        }
                    }
                }

                FilterResults results = new FilterResults();
                Collections.sort(suggestionList, new Comparator<CitySearch>() {
                    @Override
                    public int compare(CitySearch lhs, CitySearch rhs) {
                        return lhs.getIsHistory() ? -1 : 0;
                    }
                });
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null) {
                    listener.onResults((List<CitySearch>) results.values);
                }
            }
        }.filter(query);

    }


    /*public static void findColors(Context context, String query, final OnFindColorsListener listener) {
        initColorWrapperList(context);

        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {


                List<Today> suggestionList = new ArrayList<>();

                if (!(constraint == null || constraint.length() == 0)) {

                    for (Today color : sColorWrappers) {
                        if (color.getCity().toUpperCase()
                                .startsWith(constraint.toString().toUpperCase())) {

                            suggestionList.add(color);
                        }
                    }

                }

                FilterResults results = new FilterResults();
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null) {
                    listener.onResults((List<Today>) results.values);
                }
            }
        }.filter(query);

    }*/

    private static void initColorWrapperList(Context context) {

        if (citySuggestions.isEmpty()) {

            citySuggestions=loadJson(context);

        }
    }

    private static List<CitySearch> loadJson(Context context) {
        List<CitySearch> cityList = new LinkedList<>();

        try {
            InputStream is = context.getAssets().open(CITIES_FILE_NAME);
            JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));

            reader.beginArray();

            Gson gson = new GsonBuilder().create();

            while (reader.hasNext()) {
                CitySearch cityJson = gson.fromJson(reader, CitySearch.class);
                cityList.add(cityJson);

            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return cityList;
    }

    private static List<CitySearch> deserializeCity(String jsonString) throws JSONException {
        JSONObject jObject = new JSONObject(jsonString);
        JSONArray jArray = jObject.getJSONArray("citta");
        List<CitySearch> suggestion = new ArrayList<CitySearch>();
        for (int i=0; i < jArray.length(); i++)
        {
            try {
                JSONObject oneObject = jArray.getJSONObject(i);
                suggestion.add(new CitySearch(oneObject.getString("name"), oneObject.getString("country")));
            } catch (JSONException e) {
                // Errore
                System.out.println("errore json");
            }
        }

        return suggestion;
    }



}