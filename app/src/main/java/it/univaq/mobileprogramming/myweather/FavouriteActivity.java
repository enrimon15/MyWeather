package it.univaq.mobileprogramming.myweather;

import android.arch.persistence.room.Room;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import it.univaq.mobileprogramming.myweather.adapters.RecyclerViewAdapter_around;
import it.univaq.mobileprogramming.myweather.adapters.RecyclerViewAdapter_favourite;
import it.univaq.mobileprogramming.myweather.adapters.RecyclerViewAdapter_hour;
import it.univaq.mobileprogramming.myweather.database.FavDatabase;
import it.univaq.mobileprogramming.myweather.json.ParsingFavourite;
import it.univaq.mobileprogramming.myweather.json.ParsingFiveDays;
import it.univaq.mobileprogramming.myweather.json.VolleyRequest;
import it.univaq.mobileprogramming.myweather.model.ListCity;
import it.univaq.mobileprogramming.myweather.model.Preferiti;

public class FavouriteActivity extends AppCompatActivity {
    private RecyclerView rec;
    private  List<ListCity> lista = new ArrayList<ListCity>();
    private  FavDatabase favdb;
    private  RecyclerViewAdapter_favourite adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        rec = findViewById(R.id.favourite_city);
        /*int i = R.drawable.cloud;
        lista.add(new ListCity("Torreburna",i,"21°","sole","xcderv"));
        lista.add(new ListCity("Torreburna",i,"21°","sole","xcderv"));
        lista.add(new ListCity("Torreburna",i,"21°","sole","xcderv"));
        lista.add(new ListCity("Torreburna",i,"21°","sole","xcderv"));*/

        if (savedInstanceState == null){
            favdb = Room.databaseBuilder(this, FavDatabase.class, "favouriteDB")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();

        }

        Log.d("pavan", "pavan");
        rec.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter_favourite(lista);
        rec.setAdapter(adapter);
        rec.setHasFixedSize(true);

        //lista.clear();
    }

    private void getValue() {
        List<Preferiti> pref = favdb.favouriteDAO().getAll();
        Log.d("DB", "preferito");
        for (Preferiti p : pref){
            Log.d("DB", p.getNameCity());
            VolleyRequest.getInstance(this)
                    .downloadCity(p.getNameCity(), p.getCountryCity(), getResources().getString(R.string.keyOPEN), getResources().getString(R.string.keyUNITS),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response)
                                { Log.d("dentro2 db", response);
                                    if (response.length() > 0) {
                                        try {
                                            Log.d("dentro2 db", response);
                                            ParsingFavourite pars = new ParsingFavourite(response);
                                            ListCity c = pars.getCity();
                                            Log.d("DB", c.getNameCity());
                                            lista.add(c);
                                            Log.d("DB", lista.get(0).getConditionCity());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    if (adapter != null) adapter.notifyDataSetChanged();
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
        getValue();
    }





    public static void methodOnBtnClick(String cod) {
        Log.d("PROVA", "methodOnBtnClick: " + cod);
        //eliminazione record db
    }


}
