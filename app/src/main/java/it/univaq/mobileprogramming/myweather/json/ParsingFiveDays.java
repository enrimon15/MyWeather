package it.univaq.mobileprogramming.myweather.json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.univaq.mobileprogramming.myweather.R;
import it.univaq.mobileprogramming.myweather.model.Five_Days;

public class ParsingFiveDays {
    private Five_Days five;
    List<Five_Days> listaDay = new ArrayList<Five_Days>();

    /** prendo i 5 gg successivi al giorno attuale tutti alle ore 12:00 **/
    public ParsingFiveDays(String parsing) throws JSONException {
        JSONObject jor = new JSONObject(parsing);
        JSONArray lista = jor.getJSONArray("list"); //lista di 5 giorni successivi con tutte le ore

        Date date = new  Date(); //imposto la data e ora attuale
        Locale.setDefault(Locale.ITALIAN);
        int cont = 1; //cont serve al trovare l'iesimo gg a partire da oggi fino a 5
        String gg = addDays(date,cont);
        Log.d("gg", gg);
        String dayweek = getDayWeek(date,cont);
        Log.d("gg est", dayweek);

        for (int i=0; i<lista.length(); i++){ //per ogni elemento della lista
            if (cont == 6) {Log.d("for", "uscita"); break;} //se raggiungo 5 gg stop
            JSONObject oggetto = lista.getJSONObject(i);
            String data = oggetto.getString("dt_txt");
            //controlla per ogni elemento se è il gg successivo alle ore 12:00 e salva i dati necessari
            if(data.equals(gg + " 12:00:00")) {
                cont++;
                gg=addDays(date,cont);
                JSONObject main = oggetto.getJSONObject("main");
                    int min = main.getInt("temp_min"); String mintemp = min + "\u00B0";
                    int max = main.getInt("temp_max"); String maxtemp = max + "\u00B0";
                JSONArray weather = oggetto.getJSONArray("weather");
                    JSONObject icon = weather.getJSONObject(0);
                    String imm = icon.getString("icon");
                    int immagine = setImm(imm);

                Five_Days giorno = new Five_Days(dayweek,maxtemp,mintemp,immagine);
                listaDay.add(giorno);
                dayweek=getDayWeek(date,cont);
            }
        }

        //se la lista ha solo 4 gg (problemi di openweather) inserisci in più il gg attuale
        if (listaDay.size() == 4){
            JSONObject oggetto = lista.getJSONObject(0);
            dayweek = getDayWeek(date,0);
            JSONObject main = oggetto.getJSONObject("main");
            int min = main.getInt("temp_min"); String mintemp = min + "\u00B0";
            int max = main.getInt("temp_max"); String maxtemp = max + "\u00B0";
            JSONArray weather = oggetto.getJSONArray("weather");
            JSONObject icon = weather.getJSONObject(0);
            String imm = icon.getString("icon");
            int immagine = setImm(imm);
            Five_Days five = new Five_Days(dayweek, maxtemp, mintemp, immagine);
            listaDay.add(0, five);
        }
    }

    private int setImm(String imm) {
        int image = 0;
        if (imm.equals("01d")) image = R.drawable.sun;
        else if (imm.equals("01n")) image = R.drawable.sun;
        else if (imm.equals("02d")) image = R.drawable.cloudy;
        else if (imm.equals("02n")) image = R.drawable.cloudy_night;
        else if (imm.equals("03d")) image = R.drawable.cloud;
        else if (imm.equals("03n")) image = R.drawable.cloud;
        else if (imm.equals("04d")) image = R.drawable.cloud;
        else if (imm.equals("04n")) image = R.drawable.cloud;
        else if (imm.equals("09d")) image = R.drawable.rain;
        else if (imm.equals("09n")) image = R.drawable.rain;
        else if (imm.equals("10d")) image = R.drawable.rain_sun;
        else if (imm.equals("10n")) image = R.drawable.rain_night;
        else if (imm.equals("11d")) image = R.drawable.lightning;
        else if (imm.equals("11n")) image = R.drawable.lightning_night;
        else if (imm.equals("13d")) image = R.drawable.snowing;
        else if (imm.equals("13n")) image = R.drawable.snowing_night;
        else if (imm.equals("50d")) image = R.drawable.windy;
        else if (imm.equals("50n")) image = R.drawable.windy;
        return image;
    }

    /** sapere quale è il giorno attuale + 1,2,3,4,5 **/
    private String addDays(Date d, int g) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, g);
        Date x = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String form = sdf.format(x);
        return form;
    }

    /** dalla data estrapolare il giorno della settimana **/
    private String getDayWeek(Date d, int g) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, g);
        Date x = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        String form = sdf.format(x).toUpperCase();
        return form;
    }

    public List<Five_Days> getListaDay() {
        return listaDay;
    }

    public void setListaDay(List<Five_Days> listaDay) {
        this.listaDay = listaDay;
    }
}
