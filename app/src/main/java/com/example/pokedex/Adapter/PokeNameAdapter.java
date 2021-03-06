package com.example.pokedex.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.Model.Pokemon;
import com.example.pokedex.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PokeNameAdapter extends RecyclerView.Adapter<PokeNameAdapter.ViewHolder> implements Filterable{

    Context context;
    private ArrayList<Pokemon> PokeName;
    private ArrayList<Pokemon> PokeAll;
    private OnItemClickListener mListener;
    private int pic;

    String PokeImgURL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";
    String ItemImgUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/";


    public PokeNameAdapter(Context context, ArrayList<Pokemon> pokeName, int pic) {
        this.context = context;
        PokeName = pokeName;
        PokeAll = new ArrayList<>(pokeName);
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
        String url = currItem.getUrl();
        String[] urlParts = url.split("/");

        //load name
        holder.name.setText(currItem.getName());


        if(pic == 2){
            //load image
            if(Integer.parseInt(urlParts[urlParts.length - 1]) > 10090)
                holder.image.setImageResource(R.drawable.pokemon);
            else {
                Picasso
                        .get()
                        .load(PokeImgURL + urlParts[urlParts.length - 1] + ".png")
                        .into(holder.image);
            }

        }

        else if (pic == 1){
            //load item image
            if(Integer.parseInt(urlParts[urlParts.length - 1]) > 10090)
                holder.image.setImageResource(R.drawable.pokemon);
            else {
                Picasso
                        .get()
                        .load(ItemImgUrl + currItem.getName() + ".png")
                        .into(holder.image);
            }
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

    @Override
    public Filter getFilter() {
        return pokeFilter;
    }

    private Filter pokeFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Pokemon> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0) {
                filteredList.addAll(PokeAll);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Pokemon p : PokeAll){
                    if (p.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(p);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            PokeName.clear();

            PokeName.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

}
