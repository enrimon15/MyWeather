package it.univaq.mobileprogramming.myweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;

import it.univaq.mobileprogramming.myweather.json.ParsingSearch;
import it.univaq.mobileprogramming.myweather.model.Search.CitySearch;
import it.univaq.mobileprogramming.myweather.model.Search.DataHelper;


public class SlidingSearchFragment extends Fragment {
    private final String TAG = "SearchFragment";
    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;
    private FloatingSearchView mSearchView;
    private RecyclerView mSearchResultsList;
    private String mLastQuery = "";
    private Snackbar snackbar;
    private View lay;
    private static List<CitySearch> history = new ArrayList<>();

    public SlidingSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        lay = view.findViewById(R.id.search_page);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchView = view.findViewById(R.id.floating_search_view);
        mSearchResultsList = view.findViewById(R.id.search_results_list);
        mSearchView.setSearchFocused(true); //fuoco alla barra (tastiera mostrata)
    }

    @Override
    public void onResume() {
        super.onResume();
        setupFloatingSearch();
    }

    /** gestione barra ricerca **/
    private void setupFloatingSearch() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() { //ad ogni tasto premuto

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                //se la barra è vuota non mostrare suggerimenti
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {
                    //mostra l'icona circolare del progresso
                    mSearchView.showProgress();

                    //in base a quello scritto correntemente nella barra mostra 5 suggerimenti
                    DataHelper.findSuggestions(getActivity(), newQuery, 5,
                            FIND_SUGGESTION_SIMULATED_DELAY, new DataHelper.OnFindSuggestionsListener() {

                                @Override
                                public void onResults(List<CitySearch> results) {
                                    //the collapse/expand animations as necessary
                                    mSearchView.swapSuggestions(results);

                                    //let the users know that the background process has completed
                                    mSearchView.hideProgress();
                                }
                            });
                }

                Log.d(TAG, "onSearchTextChanged()");
            }
        });

        /** gestione tap suggerimento o ricerca tramite pulsante **/
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) { //tap suggerimenti

                Log.d(TAG, "onSuggestionClicked()");
                mLastQuery = ((CitySearch) searchSuggestion).getName(); //prende il nome del suggerimento toccato
                history.add((CitySearch) searchSuggestion); //aggiunge alla cronologia ricerca il suggerimento tappato

                Intent intent = new Intent(getContext(), MainActivity.class); //passa il dato ad Activity main che mostra il meteo
                intent.putExtra("cityID", "q="+mLastQuery);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onSearchAction(String query) { // tap search button
                Log.d(TAG, "onSearchAction()");
                CitySearch c = DataHelper.checkQuery(query); //controlla se la query fa match con una città della lista

                if (c != null) {
                    history.add((CitySearch) c);
                    Intent intent = new Intent(getContext(), MainActivity.class); //se si, passo il dato a main activity e mostra il meteo
                    intent.putExtra("cityID", "q="+query);
                    startActivity(intent);
                    getActivity().finish();
                }

                //se no, mostra città non trovata
                    snackbar = Snackbar.make(lay, R.string.city_not_found, snackbar.LENGTH_INDEFINITE);
                    snackbar.setDuration(3000);
                    snackbar.setAction(R.string.snakebar_remove, new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
            }
        });

        /** gestione focus barra **/
        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

                //mostra cronologia al focus (3 risultati)
                mSearchView.swapSuggestions(DataHelper.getHistory(getActivity(), 3, history));
                Log.d(TAG, "onFocus()");
            }

            @Override
            public void onFocusCleared() {

                //mostra il titolo della barra
                mSearchView.setSearchBarTitle(mLastQuery);
                Log.d(TAG, "onFocusCleared()");

            }
        });

        /** Impostare icona dei suggerimenti della cronologia **/
        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {
                CitySearch CitySearch = (CitySearch) item;


                if (CitySearch.getIsHistory()) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_history_black_24dp, null));
                    leftIcon.setAlpha(.36f); //colore grigio
                }
            }

        });

        /** when suggestion list expands/shrinks in order to move down/up thesearch results list **/
        mSearchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
            @Override
            public void onSuggestionsListHeightChanged(float newHeight) {
                mSearchResultsList.setTranslationY(newHeight);
            }
        });

        /** When the user types some text into the search field, a clear button ('x' to the right) of the search text is shown **/
        mSearchView.setOnClearSearchActionListener(new FloatingSearchView.OnClearSearchActionListener() {
            @Override
            public void onClearSearchClicked() { //cancellazione automatica
                Log.d(TAG, "onClearSearchClicked()");
            }
        });
    }


    //hide keyboard
    @Override
    public void onPause() {
        super.onPause();

        mSearchView.setSearchFocused(false);
    }

}
