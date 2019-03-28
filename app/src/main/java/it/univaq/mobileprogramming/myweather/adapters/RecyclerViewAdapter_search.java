package it.univaq.mobileprogramming.myweather.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.univaq.mobileprogramming.myweather.R;
import it.univaq.mobileprogramming.myweather.model.CitySearch;
import it.univaq.mobileprogramming.myweather.model.Five_Days;
import it.univaq.mobileprogramming.myweather.model.Today;

public class RecyclerViewAdapter_search extends RecyclerView.Adapter<RecyclerViewAdapter_search.ViewHolder> {

    private List<CitySearch> cities = new ArrayList<CitySearch>();
    private Context context;
    private List<Today> mDataSet = new ArrayList<>();

    public RecyclerViewAdapter_search(List<CitySearch> cities, Context context) {
        this.cities = cities;
        this.context = context;
    }

    public RecyclerViewAdapter_search() {    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_results_list_item, viewGroup, false);
        return new RecyclerViewAdapter_search.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        CitySearch cityFired = cities.get(i);
        holder.city.setText(cityFired.getName());
        holder.country.setText(cityFired.getCountry());
    }


    @Override
    public int getItemCount() {
        return 0;
    }

    public void swapData(List<Today> mNewDataSet) {
        mDataSet = mNewDataSet;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView city;
        private TextView country;

        public ViewHolder(View itemView) {
            super(itemView);
            city=itemView.findViewById(R.id.city_name);
            country=itemView.findViewById(R.id.city_country);
        }
    }
}
