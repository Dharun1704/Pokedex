package com.example.pokedex.Model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.pokedex.Interface.PokeAPI;
import com.example.pokedex.Adapter.PokeNameAdapter;
import com.example.pokedex.R;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements PokeNameAdapter.OnItemClickListener {
    public static final String EXTRA_NAME = "PokeName";
    public static final String EXTRA_URL =  "PokeUrl";
    public static final String EXTRA_POS = "position";

    private ArrayList<Pokemon> PokeName;
    private RecyclerView recyclerView;
    private PokeNameAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private PokeAPI pokeAPI;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 2);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        pokeAPI = retrofit.create(PokeAPI.class);
        pokeNameList();
    }

    public void pokeNameList(){
        Call<PokeList> call = pokeAPI.getPokeNameJson();
        call.enqueue(new Callback<PokeList>() {
            @Override
            public void onResponse(Call<PokeList> call, Response<PokeList> response) {
                if(!response.isSuccessful()){
                    return;
                }

                PokeName = new ArrayList<Pokemon>(Arrays.asList(response.body().results));
                adapter = new PokeNameAdapter(MainActivity.this, PokeName);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(MainActivity.this);

            }

            @Override
            public void onFailure(Call<PokeList> call, Throwable t) {
                Log.i("Error:", "Fetch: "+ t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(int position) {

        Intent detailIntent = new Intent(this, DetailActivity.class);
        Pokemon clickedPokemon = PokeName.get(position);

        detailIntent.putExtra(EXTRA_NAME, clickedPokemon.getName());
        detailIntent.putExtra(EXTRA_URL, clickedPokemon.getUrl());
        detailIntent.putExtra(EXTRA_POS, position);

        startActivity(detailIntent);
    }
}