package com.example.pokedex.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.Model.Pokemon;
import com.example.pokedex.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PokeNameAdapter extends RecyclerView.Adapter<PokeNameAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Pokemon> PokeName;
    private OnItemClickListener mListener;

    String ImgURL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";

    public PokeNameAdapter(Context context, ArrayList<Pokemon> pokeName) {
        this.context = context;
        PokeName = pokeName;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public PokeNameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.name_card, parent, false);
        return new ViewHolder(v, mListener);
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
    }

    @Override
    public int getItemCount() {
        return PokeName.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public ImageView image;


        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            name = itemView.findViewById(R.id.pokeName);
            image = itemView.findViewById(R.id.pokeImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
