package it.univaq.mobileprogramming.myweather.model;

import android.content.Context;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.univaq.mobileprogramming.myweather.SlidingSearchFragment;
import it.univaq.mobileprogramming.myweather.SplashScreen;
import it.univaq.mobileprogramming.myweather.json.ParsingSearch;

public class DataHelper {

    private static List<CitySearch> citySuggestions = new ArrayList<>();

    public interface OnFindSuggestionsListener {
        void onResults(List<CitySearch> results);
    }

    public static List<CitySearch> getHistory(Context context, int count, List<CitySearch> history) {
        List<CitySearch> suggestionListComplete = new ArrayList<>();
        CitySearch citySuggestion;

        for (int i = history.size()-1; i >= 0; i--) {
            citySuggestion = history.get(i);
            citySuggestion.setIsHistory(true);
            suggestionListComplete.add(citySuggestion);
            if (suggestionListComplete.size() == 3) {
                break;
            }
        }
        return suggestionListComplete;
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

                    if (suggestionList.isEmpty()) suggestionList.add(new CitySearch("Città non disponibile"));
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

    private static void initCityWrapperList(Context context) {

        if (citySuggestions.isEmpty()) {

            citySuggestions = ParsingSearch.getInstance(context).getList();

        }
    }


}