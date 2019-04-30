package it.univaq.mobileprogramming.myweather.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import it.univaq.mobileprogramming.myweather.FavouriteActivity;
import it.univaq.mobileprogramming.myweather.MainActivity;
import it.univaq.mobileprogramming.myweather.R;
import it.univaq.mobileprogramming.myweather.model.ListCity;

public class RecyclerViewAdapter_favourite extends RecyclerView.Adapter<RecyclerViewAdapter_favourite.ViewHolder>{
    private List<ListCity> lista_pref;

    public RecyclerViewAdapter_favourite(List<ListCity> lista){
        this.lista_pref = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fav, viewGroup, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter_favourite.ViewHolder holder, int i) {

        ListCity city = lista_pref.get(i);
        holder.name.setText(city.getName());
        holder.tempe.setText(city.getTemp());
        holder.descr.setText(city.getCondition());
        holder.ic.setImageResource(city.getWeatherIcon());
    }

    @Override
    public int getItemCount() {
        return lista_pref.size();
    }

    //definiamo il ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tempe;
        private TextView name;
        private TextView descr;
        private ImageView ic;
        private AppCompatButton but;

        public ViewHolder(View itemView) {
            super(itemView);
            tempe = itemView.findViewById(R.id.temp_fav);
            name = itemView.findViewById(R.id.text_fav);
            descr = itemView.findViewById(R.id.desc_fav);
            ic = itemView.findViewById(R.id.icon_fav);
            but = itemView.findViewById(R.id.flat_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Open another Activity and pass to it the right city
                    ListCity city = lista_pref.get(getAdapterPosition());
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("cityID", "id=" + city.getCode());
                    v.getContext().startActivity(intent);
                }
            });

            but.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    String cod = lista_pref.get(getAdapterPosition()).getCode(); //Clicked entry in your List

                    //Method from your activity
                    FavouriteActivity.methodOnBtnClick(cod);
                }
        });
        }
} }
