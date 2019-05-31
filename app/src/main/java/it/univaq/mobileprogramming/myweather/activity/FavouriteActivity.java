package it.univaq.mobileprogramming.myweather.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import it.univaq.mobileprogramming.myweather.R;
import it.univaq.mobileprogramming.myweather.adapters.RecyclerViewAdapter_favourite;
import it.univaq.mobileprogramming.myweather.database.FavDatabase;
import it.univaq.mobileprogramming.myweather.json.ParsingFavourite;
import it.univaq.mobileprogramming.myweather.json.VolleyRequest;
import it.univaq.mobileprogramming.myweather.model.ListCity;
import it.univaq.mobileprogramming.myweather.model.Preferiti;

public class FavouriteActivity extends AppCompatActivity {
    private RecyclerView rec;
    List<Preferiti> pref = new ArrayList<Preferiti>();
    private static List<ListCity> lista = new ArrayList<ListCity>();
    private static RecyclerViewAdapter_favourite adapter;
    private  static Snackbar snack;
    private  static View lay;
    private  static Context context;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_favourite);
        rec = findViewById(R.id.favourite_city);
        lay = findViewById(R.id.view_fav);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref.clear(); //resetta lista preferiti
        pref = FavDatabase.getInstance(getApplicationContext()).favouriteDAO().getAll(); //preferiti db

        rec.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter_favourite(lista);
        rec.setAdapter(adapter);
        rec.setHasFixedSize(true);
    }

    /** popola la view con i preferiti dal db **/
    private void getValue() {
        lista.clear();
        Log.d("DB", "preferito");
        for (Preferiti p : pref){ //per ogni pref nel db (salvo solo nome e country) scarica i dati
            VolleyRequest.getInstance(getApplicationContext())
                    .downloadCity(p.getNameCity(), p.getCountryCity(), getResources().getString(R.string.keyOPEN), getResources().getString(R.string.keyUNITS),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response)
                                {
                                    if (response.length() > 0) {
                                        try { //parsing dati
                                            ParsingFavourite pars = new ParsingFavourite(response);
                                            ListCity c = pars.getCity();
                                            lista.add(c); //aggiungi ogni city parsata ad una lista
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    if (adapter != null) adapter.notifyDataSetChanged(); //aggiorna l'adapter
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                }
                            });
        }
    }

   @Override
    public void onResume() {
        super.onResume();
        getValue(); //popola la view con i preferiti
    }




    /** gestione tap su rimuovi **/
    public static void methodOnBtnClick(final ListCity lc) {
        //eliminazione record db
        new Thread(new Runnable() {
            @Override
            public void run() {
                FavDatabase.getInstance(context).favouriteDAO().delete(lc.getCode());
            }
        }).start();
        snack = Snackbar.make(lay, R.string.city_removed, Snackbar.LENGTH_SHORT);
        snack.setDuration(3000);
        snack.show();
        lista.remove(lc);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search_button:
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.position_button:
                Intent intent1 = new Intent(getApplicationContext(), AroundMeActivity.class);
                startActivity(intent1);
                finish();
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return false;
        }

    }

}
