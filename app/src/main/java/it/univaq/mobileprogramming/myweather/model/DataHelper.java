package it.univaq.mobileprogramming.myweather.model;

import android.content.Context;
import android.widget.Filter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.univaq.mobileprogramming.myweather.AroundMeActivity;
import it.univaq.mobileprogramming.myweather.SlidingSearchFragment;
import it.univaq.mobileprogramming.myweather.SplashScreen;

public class DataHelper {

    private static final String CITIES_FILE_NAME = "city_list.json";

    private static List<CitySearch> citySuggestions = new ArrayList<>();

    public interface OnFindSuggestionsListener {
        void onResults(List<CitySearch> results);
    }

    public static List<CitySearch> getHistory(Context context, int count) {

        List<CitySearch> suggestionList = new ArrayList<>();
        CitySearch citySuggestion;
        suggestionList.addAll(SlidingSearchFragment.getHistoryList());
        for (int i = 0; i < suggestionList.size(); i++) {
            citySuggestion = suggestionList.get(i);
            citySuggestion.setIsHistory(true);
            if (i == count) {
                break;
            }
        }
        return suggestionList;
    }

    public static void resetSuggestionsHistory() {
        for (CitySearch citySuggestion : citySuggestions) {
            citySuggestion.setIsHistory(false);
        }
    }

    public static void findSuggestions(final Context context, String query, final int limit, final long simulatedDelay, final OnFindSuggestionsListener listener) {
        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                try {
                    Thread.sleep(simulatedDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                DataHelper.resetSuggestionsHistory();
                initCityWrapperList(context);
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

                    //if (suggestionList.isEmpty()) suggestionList.add(new CitySearch("No suggestions available", ""));
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

                if (listener != null & results.values != null) {
                    listener.onResults((List<CitySearch>) results.values);
                }
                else {
                    List<CitySearch> vuoto = new ArrayList<>();
                    vuoto.add(new CitySearch("No suggestions available"));
                    listener.onResults(vuoto);
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

    private static void initCityWrapperList(Context context) {

        if (citySuggestions.isEmpty()) {

            citySuggestions= SplashScreen.getList();

        }
    }


}