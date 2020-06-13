package com.example.pokedex.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.Model.Pokemon;
import com.example.pokedex.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PokeNameAdapter extends RecyclerView.Adapter<PokeNameAdapter.ViewHolder> {

    Context context;
    private ArrayList<Pokemon> PokeName;
    private OnItemClickListener mListener;
    private int pic;

    String PokeImgURL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";
    String ItemImgUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/";


    public PokeNameAdapter(Context context, ArrayList<Pokemon> pokeName, int pic) {
        this.context = context;
        PokeName = pokeName;
        this.pic = pic;
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

        if(pic == 1) {
            //load image
            if (position <= 807) {
                Picasso
                        .get()
                        .load(PokeImgURL + (position + 1) + ".png")
                        .into(holder.image);
            } else {
                Picasso
                        .get()
                        .load(PokeImgURL + (position + 9193) + ".png")
                        .into(holder.image);

            }
        }

        else if (pic == 2){
            //load item image
            Picasso
                    .get()
                    .load(ItemImgUrl + currItem.getName() + ".png")
                    .into(holder.image);
        }


        else if (pic == 0){
            //remove image
            holder.image.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return PokeName.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public ImageView image;
        public CardView card;


        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            name = itemView.findViewById(R.id.pokeName);
            image = itemView.findViewById(R.id.pokeImage);
            card = itemView.findViewById(R.id.cardView);

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
