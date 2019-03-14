package it.univaq.mobileprogramming.myweather.adapters;

 import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import it.univaq.mobileprogramming.myweather.R;
import it.univaq.mobileprogramming.myweather.model.Five_Day;


public class RecyclerViewAdapter_hour extends RecyclerView.Adapter<RecyclerViewAdapter_hour.ViewHolder>
{

    private List<Five_Day> hours;
    private Context context;

    public RecyclerViewAdapter_hour(List<Five_Day> hours, Context context) {
        this.hours = hours;
        this.context = context;
    }

    // facciamo l'inflate (gonfiaggio) lo riportiamo sul ViewHolder -> grazie al quale andrà a richiamare i vari componenti
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_hours, parent, false);
        return new ViewHolder(v);
    }


    //impostare gli oggetti presi dalla lista popolata da classi "model"
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Five_Day hour = hours.get(position);
        holder.tempmax.setText(hour.getMax_temp());
        holder.tempmin.setText(hour.getMin_temp());
        holder.ora.setText(hour.getday());
        holder.weatherImage.setImageResource(hour.getWeatherIcon());
    }

    //restituire la dimensione della lista
    @Override
    public int getItemCount() {
        return hours.size();
    }

    //definiamo il ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tempmax;
        private TextView tempmin;
        private TextView ora;
        private ImageView weatherImage;

        public ViewHolder(View itemView) {
           super(itemView);
           tempmax = itemView.findViewById(R.id.weather_min_temp);
           tempmin = itemView.findViewById(R.id.weather_max_temp);
           ora = itemView.findViewById(R.id.hour);
           weatherImage = itemView.findViewById(R.id.weather_icon);
         }
    }
}
