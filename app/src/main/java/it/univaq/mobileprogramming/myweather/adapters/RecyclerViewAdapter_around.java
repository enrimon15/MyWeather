package it.univaq.mobileprogramming.myweather.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import it.univaq.mobileprogramming.myweather.activity.MainActivity;
import it.univaq.mobileprogramming.myweather.R;
import it.univaq.mobileprogramming.myweather.model.ListCity;

public class RecyclerViewAdapter_around extends RecyclerView.Adapter<RecyclerViewAdapter_around.ViewHolder> {
    private List<ListCity> lista_around;

    public RecyclerViewAdapter_around(List<ListCity> lista){
        this.lista_around = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.city_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        ListCity city = lista_around.get(i);
        holder.title.setText(city.getName());
        holder.temp.setText(city.getTemp());
        holder.condition.setText(city.getCondition());
        holder.image.setImageResource(city.getWeatherIcon());
    }

    @Override
    public int getItemCount() {
        return lista_around.size();
    }

    //definiamo il ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView temp;
        private TextView title;
        private TextView condition;
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            temp = itemView.findViewById(R.id.temp_list);
            title = itemView.findViewById(R.id.text_list);
            condition = itemView.findViewById(R.id.desc_list);
            image = itemView.findViewById(R.id.icon_list);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Open Main Activity and pass to it the right city (ID)
                    ListCity city = lista_around.get(getAdapterPosition());
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("cityID", "id=" + city.getCode());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }




}
