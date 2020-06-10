package com.example.pokedex.Model;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.Adapter.PokeDetailAdapter;
import com.example.pokedex.Interface.PokeAPI;
import com.example.pokedex.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.pokedex.Model.MainActivity.EXTRA_NAME;
import static com.example.pokedex.Model.MainActivity.EXTRA_POS;
import static com.example.pokedex.Model.MainActivity.EXTRA_URL;

public class DetailActivity extends AppCompatActivity {

    ArrayList<PokeDetail> pokeDetails;
    ImageView PokeImage;
    TextView PokeName, tv_height, tv_weight;
    int id, height, weight;
    private PokeAPI pokeAPI;

    String baseUrl = "https://pokeapi.co/api/v2/";
    String ImgURL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";

    private static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_detail_card);

        Intent intent = getIntent();
        String PokemonName = intent.getStringExtra(EXTRA_NAME);
        String PokemonUrl = intent.getStringExtra(EXTRA_URL);
        id = intent.getIntExtra(EXTRA_POS, -1) + 1;

        PokeName = findViewById(R.id.detail_name);
        PokeImage = findViewById(R.id.detail_image);
        tv_height = findViewById(R.id.height_card);
        tv_weight = findViewById(R.id.weight_card);

        PokeName.setText(PokemonName);
        Picasso
                .get()
                .load(ImgURL + id + ".png")
                .fit()
                .centerCrop()
                .into(PokeImage);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();



        //PokemonDetailList();
    }
}
