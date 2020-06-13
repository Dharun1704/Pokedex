package com.example.pokedex.Model;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.pokedex.Interface.PokeAPI;
import com.example.pokedex.Adapter.PokeNameAdapter;
import com.example.pokedex.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_NAME = "PokeName";
    public static final String EXTRA_POS = "position";
    int pic;

    private ArrayList<Pokemon> PokeName;
    private ArrayList<Pokemon> PokeType;
    private ArrayList<Pokemon> PokeLocation;
    private ArrayList<Pokemon> PokeItem;
    private ArrayList<Pokemon> PokeRegion;
    private ArrayList<TypePokeInnerList> TypeName;
    private ArrayList<Pokemon> typeCurrPoke;

    private RecyclerView recyclerView;
    private PokeNameAdapter nameAdapter;
    private PokeNameAdapter typeAdapter;
    private PokeNameAdapter locationAdapter;
    private PokeNameAdapter itemAdapter;
    private PokeNameAdapter regionAdapter;
    private PokeNameAdapter typeNameAdapter;

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
        getNameList();
        setBtnListener();
    }

    public void setBtnListener(){

        Button btnName = findViewById(R.id.btn_name);
        Button btnType = findViewById(R.id.btn_type);
        Button btnLocation = findViewById(R.id.btn_location);
        Button btnItem = findViewById(R.id.btn_item);
        Button btnRegion = findViewById(R.id.btn_region);

        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNameList();
            }
        });

        btnItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItemList();
            }
        });

        btnType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTypeList();
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationList();
            }
        });

        btnRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRegionList();
            }
        });
    }

    public void getNameList(){
        Call<PokeList> call = pokeAPI.getPokeNameJson();
        call.enqueue(new Callback<PokeList>() {
            @Override
            public void onResponse(Call<PokeList> call, Response<PokeList> response) {
                if(!response.isSuccessful()){
                    return;
                }

                pic = 1;
                PokeName = new ArrayList<Pokemon>(Arrays.asList(response.body().results));
                nameAdapter = new PokeNameAdapter(MainActivity.this, PokeName, pic);
                recyclerView.setLayoutManager(mLayoutManager);
                nameAdapter.setOnItemClickListener(new PokeNameAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
                        Pokemon clickedPokemon = PokeName.get(position);

                        detailIntent.putExtra(EXTRA_NAME, clickedPokemon.getName());
                        detailIntent.putExtra(EXTRA_POS, position);

                        startActivity(detailIntent);
                    }
                });
                recyclerView.setAdapter(nameAdapter);
            }

            @Override
            public void onFailure(Call<PokeList> call, Throwable t) {
                Log.i("Error:", "Fetch: "+ t.getMessage());
            }
        });
    }

    public void getItemList(){
        Call<PokeList> call2 = pokeAPI.getItemsNameJson();
        call2.enqueue(new Callback<PokeList>() {
            @Override
            public void onResponse(Call<PokeList> call, Response<PokeList> response) {
                if (!response.isSuccessful()){
                    return;
                }

                pic = 2;
                PokeItem = new ArrayList<Pokemon>(Arrays.asList(response.body().results));
                itemAdapter = null;
                itemAdapter = new PokeNameAdapter(MainActivity.this, PokeItem, 2);
                recyclerView.setAdapter(itemAdapter);
            }

            @Override
            public void onFailure(Call<PokeList> call, Throwable t) {
                Log.i("Error:", "Fetch: "+ t.getMessage());
            }
        });
    }

    public void getTypeList(){
        Call<PokeList> call3 = pokeAPI.getPokeTypeJson();
        call3.enqueue(new Callback<PokeList>() {
            @Override
            public void onResponse(Call<PokeList> call, Response<PokeList> response) {
                if (!response.isSuccessful()){
                    return;
                }

                pic = 0;
                PokeType = new ArrayList<>(Arrays.asList(response.body().results));
                typeAdapter = null;
                typeAdapter = new PokeNameAdapter(MainActivity.this, PokeType, pic);
                recyclerView.setAdapter(typeAdapter);

                typeAdapter.setOnItemClickListener(new PokeNameAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        typePokemon(position+1);
                    }
                });
            }

            @Override
            public void onFailure(Call<PokeList> call, Throwable t) {
                Log.i("Error:", "Fetch: "+ t.getMessage());
            }
        });
    }

    public void typePokemon(int i){
        Call<TypePokeList> call5 = pokeAPI.getTypePokemon(i);
        call5.enqueue(new Callback<TypePokeList>() {
            @Override
            public void onResponse(Call<TypePokeList> call, Response<TypePokeList> response) {
                if (!response.isSuccessful()){
                    return;
                }

                pic = 3;
                typeCurrPoke = new ArrayList<>();
                TypeName = new ArrayList<>(Arrays.asList(response.body().pokemon));
                for (TypePokeInnerList il : TypeName){
                    typeCurrPoke.add(il.pokemon);
                }

                typeNameAdapter = null;
                typeNameAdapter = new PokeNameAdapter(MainActivity.this, typeCurrPoke, 0);
                recyclerView.setAdapter(typeNameAdapter);
            }

            @Override
            public void onFailure(Call<TypePokeList> call, Throwable t) {
                Log.i("Error:", "Fetch: "+ t.getMessage());
            }
        });
    }


    public void getLocationList(){
        Call<PokeList> call4 = pokeAPI.getLocationNameJson();
        call4.enqueue(new Callback<PokeList>() {
            @Override
            public void onResponse(Call<PokeList> call, Response<PokeList> response) {
                if (!response.isSuccessful()){
                    return;
                }

                pic = 0;
                PokeLocation = new ArrayList<>(Arrays.asList(response.body().results));
                locationAdapter = null;
                locationAdapter = new PokeNameAdapter(MainActivity.this, PokeLocation, pic);
                recyclerView.setAdapter(locationAdapter);
            }

            @Override
            public void onFailure(Call<PokeList> call, Throwable t) {
                Log.i("Error:", "Fetch: "+ t.getMessage());
            }
        });
    }

    public void getRegionList(){
        Call<PokeList> call6 = pokeAPI.getRegionNameJson();
        call6.enqueue(new Callback<PokeList>() {
            @Override
            public void onResponse(Call<PokeList> call, Response<PokeList> response) {
                if (!response.isSuccessful()){
                    return;
                }

                pic = 0;
                PokeRegion = new ArrayList<>(Arrays.asList(response.body().results));
                regionAdapter = null;
                regionAdapter = new PokeNameAdapter(MainActivity.this, PokeRegion, pic);
                recyclerView.setAdapter(regionAdapter);
            }

            @Override
            public void onFailure(Call<PokeList> call, Throwable t) {
                Log.i("Error:", "Fetch: "+ t.getMessage());
            }
        });
    }

}