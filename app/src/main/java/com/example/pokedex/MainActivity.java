package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

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
        Call<PokeList> call = pokeAPI.getPokeJson();
        call.enqueue(new Callback<PokeList>() {
            @Override
            public void onResponse(Call<PokeList> call, Response<PokeList> response) {
                if(!response.isSuccessful()){
                    return;
                }

                PokeName = new ArrayList<Pokemon>(Arrays.asList(response.body().results));
                adapter = new PokeNameAdapter(PokeName);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onFailure(Call<PokeList> call, Throwable t) {
                Log.i("Error:", "Fetch: "+ t.getMessage());
            }
        });
    }
}