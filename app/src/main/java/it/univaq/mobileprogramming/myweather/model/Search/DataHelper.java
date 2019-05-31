package it.univaq.mobileprogramming.myweather.model.Search;

import android.content.Context;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.univaq.mobileprogramming.myweather.R;
import it.univaq.mobileprogramming.myweather.json.ParsingSearch;

public class DataHelper {

    private static List<CitySearch> citySuggestions = new ArrayList<>();

    public interface OnFindSuggestionsListener {
        void onResults(List<CitySearch> results);
    }

    /** cronologia ultime città ricercate (in base al count passato) **/
    public static List<CitySearch> getHistory(Context context, int count, List<CitySearch> history) {
        List<CitySearch> suggestionListComplete = new ArrayList<>();
        CitySearch citySuggestion;

        for (int i = history.size()-1; i >= 0; i--) {
            citySuggestion = history.get(i);
            citySuggestion.setIsHistory(true);
            suggestionListComplete.add(citySuggestion);
            if (suggestionListComplete.size() == count) {
                break;
            }
        }
        return suggestionListComplete;
    }

    /** controlla che la stringa cercata è presente nella lista di città **/
    public static CitySearch checkQuery(String query) {
        CitySearch city = null;
        String search = query.substring(0, 1).toUpperCase() + query.substring(1);

        for (CitySearch c: citySuggestions){ //controllo se la query fa match con una città della lista
            if (c.getName().equals(search)) {
                city = c;
            }
        }
        return city;
    }

    /** resetta la lista della cronologia quando si ricerca **/
    public static void resetSuggestionsHistory() {
        for (CitySearch citySuggestion : citySuggestions) {
            citySuggestion.setIsHistory(false);
        }
    }

    /** trova suggerimenti in base alla query corrente **/
    public static void findSuggestions(final Context context, String query, final int limit, final long simulatedDelay, final OnFindSuggestionsListener listener) {
        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                try {
                    Thread.sleep(simulatedDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                DataHelper.resetSuggestionsHistory(); //clear suggerimenti
                initCityWrapperList(context); //prende la lista di tutte le città
                List<CitySearch> suggestionList = new ArrayList<>();
                if (!(constraint == null || constraint.length() == 0)) {

                    for (CitySearch suggestion : citySuggestions) { //per ogni città nella listra trova match con la query
                        if (suggestion.getName().toUpperCase()
                                .startsWith(constraint.toString().toUpperCase())) {

                            suggestionList.add(suggestion); //se fa match aggiungi ad una nuova lista
                            if (limit != -1 && suggestionList.size() == limit) { //quando raggiungo il numero di dugg specificati stop
                                break;
                            }
                        }

                    }

                    //se non trovo nessun match
                    if (suggestionList.isEmpty()) suggestionList.add(new CitySearch("Città non disponibile"));
                }

                //filtra i risultati in base alle esigenze
                FilterResults results = new FilterResults();
                /*Collections.sort(suggestionList, new Comparator<CitySearch>() {
                    @Override
                    public int compare(CitySearch lhs, CitySearch rhs) {
                        return lhs.getIsHistory() ? -1 : 0;
                    }
                });*/
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            //mostra i risultati appena ottenuti
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null & results.values != null) {
                    listener.onResults((List<CitySearch>) results.values);
                }
            }
        }.filter(query);

    }

    /** prende la lista delle città del mondo parsate **/
    private static void initCityWrapperList(Context context) {
        if (citySuggestions.isEmpty()) {
            citySuggestions = ParsingSearch.getInstance(context).getList();
        }
    }


}