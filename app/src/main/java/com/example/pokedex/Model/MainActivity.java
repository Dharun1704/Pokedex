package com.example.pokedex.Model;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Toast;

import com.example.pokedex.Interface.PokeAPI;
import com.example.pokedex.Adapter.PokeNameAdapter;
import com.example.pokedex.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final String EXTRA_NAME = "PokeName";
    public static final String EXTRA_POS = "position";
    int pic;

    private ArrayList<Pokemon> PokeName;
    private ArrayList<Pokemon> PokeType;
    private ArrayList<Pokemon> PokeLocation;
    private ArrayList<Pokemon> PokeItem;
    private ArrayList<Pokemon> PokeRegion;
    private ArrayList<Pokemon> favorites;
    private ArrayList<TypePokeInnerList> TypeName;
    private ArrayList<Pokemon> typeCurrPoke;
    private Database mDatabase;

    private RecyclerView recyclerView;
    private PokeNameAdapter rAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private PokeAPI pokeAPI;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.nav_action);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        for (int i = 0; i < 6; i++) {
            navigationView.getMenu().getItem(i).setActionView(R.layout.menu_image);
        }

        drawerLayout = findViewById(R.id.draw_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        mDatabase = new Database(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        pokeAPI = retrofit.create(PokeAPI.class);
        getNameList();

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            if (getSupportActionBar().getTitle().toString().equals("Poke Dex")) {

                if (direction == ItemTouchHelper.LEFT) {

                    Pokemon favPoke = PokeName.get(position);
                    Cursor data = mDatabase.getData();
                    favorites = new ArrayList<>();

                    while (data.moveToNext()) {
                        String name = data.getString(1);
                        String url = data.getString(2);
                        Pokemon pTemp = new Pokemon();
                        pTemp.setName(name);
                        pTemp.setUrl(url);
                        favorites.add(pTemp);
                    }

                    boolean isExists = false;
                    for (int i = 0; i < favorites.size(); i++) {
                        if (favorites.get(i).getName().equals(favPoke.getName())) {
                            isExists = true;
                        }
                    }
                    if (favPoke != null) {
                        if (!isExists)
                            addFavorite(favPoke);
                        else
                            Toast.makeText(MainActivity.this, favPoke.getName() + " already exists in favorites", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Unable to find pokemon", Toast.LENGTH_SHORT).show();
                    }
                    getNameList();
                }

                if (direction == ItemTouchHelper.RIGHT) {
                    Toast.makeText(MainActivity.this, "You are not in favorite page to delete a pokemon", Toast.LENGTH_SHORT).show();
                    getNameList();
                }
            }

            else if (getSupportActionBar().getTitle().toString().equals("Favorites")) {
                if (direction == ItemTouchHelper.LEFT){
                    Toast.makeText(MainActivity.this, "You are in favorites page", Toast.LENGTH_SHORT).show();
                    getFavoriteList();
                    String title = getSupportActionBar().getTitle().toString();
                    switch (title) {
                        case "Item Dex":
                            getItemList();
                            break;
                        case "Type Dex":
                            getTypeList();
                            break;
                        case "Location Dex":
                            getLocationList();
                            break;
                        case "Region Dex":
                            getRegionList();
                            break;
                    }
                }

                if (direction ==  ItemTouchHelper.RIGHT) {
                    Pokemon favPoke = favorites.get(position);
                    String name = favPoke.getName();
                    int deleteRow = mDatabase.deleteData(name);
                    if (deleteRow > 0) {
                        Toast.makeText(MainActivity.this, favPoke.getName() + " deleted from favorites", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(MainActivity.this, "Not able to delete " + favPoke.getName() , Toast.LENGTH_SHORT).show();
                    getFavoriteList();
                }
            }

            else {
                Toast.makeText(MainActivity.this, "Only pokemon can be added or deleted", Toast.LENGTH_SHORT).show();
                String title = getSupportActionBar().getTitle().toString();
                switch (title) {
                    case "Item Dex":
                        getItemList();
                        break;
                    case "Type Dex":
                        getTypeList();
                        break;
                    case "Location Dex":
                        getLocationList();
                        break;
                    case "Region Dex":
                        getRegionList();
                        break;
                }
            }
        }
    };

    public void addFavorite(Pokemon p) {
        String name = p.getName();
        String url = p.getUrl();
        boolean insertData = mDatabase.addData(name, url);

        if (insertData) {
            Toast.makeText(MainActivity.this, p.getName() + " added to favorites", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(MainActivity.this, "Unable to add " + p.getName() + " to favorites", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.PokeDex:
                getNameList();
                getSupportActionBar().setTitle("Poke Dex");
                break;
            case R.id.ItemDex:
                getItemList();
                getSupportActionBar().setTitle("Item Dex");
                break;
            case R.id.TypeDex:
                getTypeList();
                getSupportActionBar().setTitle("Type Dex");
                break;
            case R.id.LocationDex:
                getLocationList();
                getSupportActionBar().setTitle("Location Dex");
                break;
            case R.id.RegionDex:
                getRegionList();
                getSupportActionBar().setTitle("Region Dex");
                break;
            case R.id.Favorite:
                getFavoriteList();
                getSupportActionBar().setTitle("Favorites");
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getNameList(){
        Call<PokeList> call = pokeAPI.getPokeNameJson();
        call.enqueue(new Callback<PokeList>() {
            @Override
            public void onResponse(Call<PokeList> call, Response<PokeList> response) {
                if(!response.isSuccessful()){
                    return;
                }

                pic = 2;
                PokeName = new ArrayList<>(Arrays.asList(response.body().results));
                rAdapter = null;
                rAdapter = new PokeNameAdapter(MainActivity.this, PokeName, pic);
                mLayoutManager = new GridLayoutManager(MainActivity.this, 2);
                recyclerView.setLayoutManager(mLayoutManager);
                rAdapter.setOnItemClickListener(new PokeNameAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
                        Pokemon clickedPokemon = PokeName.get(position);
                        detailIntent.putExtra(EXTRA_NAME, clickedPokemon.getName());
                        detailIntent.putExtra(EXTRA_POS, clickedPokemon.getUrl());

                        startActivity(detailIntent);
                    }
                });
                recyclerView.setAdapter(rAdapter);
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(recyclerView);
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

                pic = 1;
                PokeItem = new ArrayList<Pokemon>(Arrays.asList(response.body().results));
                rAdapter = null;
                rAdapter = new PokeNameAdapter(MainActivity.this, PokeItem, pic);
                recyclerView.setAdapter(rAdapter);
                mLayoutManager = new GridLayoutManager(MainActivity.this, 2);
                recyclerView.setLayoutManager(mLayoutManager);
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
                rAdapter = null;
                rAdapter = new PokeNameAdapter(MainActivity.this, PokeType, pic);
                recyclerView.setAdapter(rAdapter);
                mLayoutManager = new GridLayoutManager(MainActivity.this, 2);
                recyclerView.setLayoutManager(mLayoutManager);

                rAdapter.setOnItemClickListener(new PokeNameAdapter.OnItemClickListener() {
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

                pic = 2;
                typeCurrPoke = new ArrayList<>();
                TypeName = new ArrayList<>(Arrays.asList(response.body().pokemon));
                for (TypePokeInnerList il : TypeName){
                    typeCurrPoke.add(il.pokemon);
                }

                rAdapter = null;
                rAdapter = new PokeNameAdapter(MainActivity.this, typeCurrPoke, pic);
                recyclerView.setAdapter(rAdapter);
                mLayoutManager = new GridLayoutManager(MainActivity.this, 2);
                recyclerView.setLayoutManager(mLayoutManager);
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
                rAdapter = null;
                rAdapter = new PokeNameAdapter(MainActivity.this, PokeLocation, pic);
                recyclerView.setAdapter(rAdapter);
                mLayoutManager = new GridLayoutManager(MainActivity.this, 2);
                recyclerView.setLayoutManager(mLayoutManager);
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
                rAdapter = null;
                rAdapter = new PokeNameAdapter(MainActivity.this, PokeRegion, pic);
                recyclerView.setAdapter(rAdapter);
                mLayoutManager = new GridLayoutManager(MainActivity.this, 2);
                recyclerView.setLayoutManager(mLayoutManager);
            }

            @Override
            public void onFailure(Call<PokeList> call, Throwable t) {
                Log.i("Error:", "Fetch: "+ t.getMessage());
            }
        });
    }

    public void getFavoriteList() {
        Cursor data = mDatabase.getData();
        favorites = new ArrayList<>();
        while (data.moveToNext()) {
            String name = data.getString(1);
            String url = data.getString(2);
            Pokemon p = new Pokemon();
            p.setName(name);
            p.setUrl(url);
            favorites.add(p);
        }
        rAdapter = null;
        rAdapter = new PokeNameAdapter(MainActivity.this,favorites, 2);
        mLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        rAdapter.setOnItemClickListener(new PokeNameAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
                Pokemon clickedPokemon = PokeName.get(position);
                detailIntent.putExtra(EXTRA_NAME, clickedPokemon.getName());
                detailIntent.putExtra(EXTRA_POS, clickedPokemon.getUrl());

                startActivity(detailIntent);
            }
        });
        recyclerView.setAdapter(rAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.pokemon_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}