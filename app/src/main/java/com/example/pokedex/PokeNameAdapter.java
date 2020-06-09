package com.example.pokedex;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PokeNameAdapter extends RecyclerView.Adapter<PokeNameAdapter.ViewHolder> {

    private ArrayList<Pokemon> PokeName;
    String ImgURL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";

    public PokeNameAdapter(ArrayList<Pokemon> pokeName) {
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
        holder.name.setText(currItem.getName());

        Picasso
                .get()
                .load(ImgURL + position + ".png")
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return PokeName.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.pokeName);
            image = itemView.findViewById(R.id.pokeImage);
        }
    }
}
