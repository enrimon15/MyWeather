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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.univaq.mobileprogramming.myweather.SlidingSearchFragment;
import it.univaq.mobileprogramming.myweather.SplashScreen;

public class DataHelper {

    private static List<CitySearch> citySuggestions = new ArrayList<>();

    public interface OnFindSuggestionsListener {
        void onResults(List<CitySearch> results);
    }

    public static List<CitySearch> getHistory(Context context, int count) {

        List<CitySearch> suggestionList = new ArrayList<>();
        CitySearch citySuggestion;
        List<CitySearch> history = SlidingSearchFragment.getHistoryList();
        for (int i = 0; i < history.size(); i++) {
            citySuggestion = history.get(i);
            citySuggestion.setIsHistory(true);
            suggestionList.add(citySuggestion);
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
                    vuoto.add(new CitySearch("No suggestions available", ""));
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