package it.univaq.mobileprogramming.myweather;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        pref.clear();
        pref = FavDatabase.getInstance(getApplicationContext()).favouriteDAO().getAll();

        Log.d("pavan", "pavan");
        rec.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter_favourite(lista);
        rec.setAdapter(adapter);
        rec.setHasFixedSize(true);
    }

    private void getValue() {
        lista.clear();
        Log.d("DB", "preferito");
        for (Preferiti p : pref){
            Log.d("DB", p.getNameCity());
            VolleyRequest.getInstance(getApplicationContext())
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
                                            Log.d("DB", c.getName());
                                            lista.add(c);
                                            Log.d("DB", lista.get(0).getCondition());
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





    public static void methodOnBtnClick(final ListCity lc) {
        //eliminazione record db
        new Thread(new Runnable() {
            @Override
            public void run() {
                FavDatabase.getInstance(context).favouriteDAO().delete(lc.getCode());
            }
        }).start();
        snack = Snackbar.make(lay, "Città rimossa dai preferiti", Snackbar.LENGTH_SHORT);
        snack.setDuration(3000);
        snack.show();
        lista.remove(lc);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        //MenuItem searchView = menu.findItem(R.id.search_item); PER APRIRE BARRA RICERCA
        //searchView.expandActionView();


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
