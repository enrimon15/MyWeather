package it.univaq.mobileprogramming.myweather;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.github.pavlospt.CircleView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    //private Snackbar snackbar;
    //private View view;

    private TextView t2_city,t4_date;
    private CircleView t1_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //view = findViewById(R.id.view);

        t1_temp = (CircleView)findViewById(R.id.weather_result);
        t2_city = (TextView)findViewById(R.id.city_country);
        t4_date = (TextView)findViewById(R.id.current_date);

        try {
            find_weather();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    public void find_weather() throws JSONException {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://api.openweathermap.org/data/2.5/weather?q=Torrebruna,it&appid=73f45256d96f6980fc804cca915873ea&units=metric&lang=it", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                // called when response HTTP status is "200 OK"
                String in = new String(responseBody);
                String response = new String(responseBody);
                try {
                    parseMethod(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                // called when response HTTP status is "404 error"
                /*snackbar = Snackbar.make(view, "errore", snackbar.LENGTH_INDEFINITE);
                snackbar.setDuration(3000);
                snackbar.setAction("RIMUOVI", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();*/
                Toast.makeText(getApplicationContext(), "Errore: Inserire Città valida!", Toast.LENGTH_SHORT).show();
            }

        }); }

        public void parseMethod(String apiString) throws JSONException {
        String url = apiString;
        JSONObject jor = new JSONObject(url);

                    JSONObject main_object = jor.getJSONObject("main");
                    JSONObject country_object = jor.getJSONObject("sys");
                    JSONArray array = jor.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    Double temp= main_object.getDouble("temp");
                    String country= String.valueOf(country_object.getString("country"));
                    String description = object.getString("description");
                    String city = jor.getString("name");

                    String temperatura = String.format ("%.1f", temp);
                    t1_temp.setTitleText(temperatura);
                    t2_city.setText(city + ", " + country);
                    t1_temp.setSubtitleText(description);

                    Date data = new  Date();
                    Locale.setDefault(Locale.ITALIAN);
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM ");
                    String formatted_date = sdf.format(data);

                    t4_date.setText(formatted_date);
            }
    }
