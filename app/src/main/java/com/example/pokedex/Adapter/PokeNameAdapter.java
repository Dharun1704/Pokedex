package com.example.pokedex.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.DetailActivity;
import com.example.pokedex.ItemClickListener;
import com.example.pokedex.MainActivity;
import com.example.pokedex.Model.Pokemon;
import com.example.pokedex.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PokeNameAdapter extends RecyclerView.Adapter<PokeNameAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Pokemon> PokeName;
    String ImgURL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";

    public PokeNameAdapter(Context context, ArrayList<Pokemon> pokeName) {
        this.context = context;
        PokeName = pokeName;
    }

    @NonNull
    @Override
    public PokeNameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.name_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PokeNameAdapter.ViewHolder holder, int position) {
        Pokemon currItem = PokeName.get(position);

        //load name
        holder.name.setText(currItem.getName());

        //load image
        if(position <= 807) {
            Picasso
                    .get()
                    .load(ImgURL + (position + 1) + ".png")
                    .into(holder.image);
        }
        else {
            Picasso
                    .get()
                    .load(ImgURL + (position + 9193) + ".png")
                    .into(holder.image);

        }

        //Event
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(context, "Pokemon: "+currItem.getName(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return PokeName.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView name;
        public ImageView image;

        ItemClickListener itemClickListener;

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.pokeName);
            image = itemView.findViewById(R.id.pokeImage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition());
        }
    }
}
