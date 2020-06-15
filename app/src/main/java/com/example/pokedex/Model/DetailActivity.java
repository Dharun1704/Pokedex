package com.example.pokedex.Model;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pokedex.Interface.PokeAPI;
import com.example.pokedex.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.pokedex.Model.MainActivity.EXTRA_NAME;
import static com.example.pokedex.Model.MainActivity.EXTRA_POS;

public class DetailActivity extends AppCompatActivity {

    PokeAPI getApi;
    private PokemonClass pClass;

    ImageView PokeImage;
    TextView PokeName, tv_height, tv_weight, tv_type, tv_ability, tv_id, tv_exp;
    TextView hp, speed, attack, defense, spAtk, spDfs;
    int id, ID, exp;
    float height, weight;

    String baseUrl = "https://pokeapi.co/api/v2/";
    String ImgURL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";
    String typeS, abilityS, HP, Speed, Attack, Defense, SpAtk, SpDfs;

    private static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_detail_card);

        tv_type = findViewById(R.id.text_type);
        tv_ability = findViewById(R.id.text_ability);

        Intent intent = getIntent();
        String PokemonName = intent.getStringExtra(EXTRA_NAME);
        id = intent.getIntExtra(EXTRA_POS, -1) + 1;

        PokeName = findViewById(R.id.detail_name);
        PokeImage = findViewById(R.id.detail_image);
        tv_height = findViewById(R.id.height_card);
        tv_weight = findViewById(R.id.weight_card);
        tv_id = findViewById(R.id.id_card);
        tv_exp = findViewById(R.id.exp_card);

        hp = findViewById(R.id.stat_hp);
        attack = findViewById(R.id.stat_attack);
        defense = findViewById(R.id.stat_defense);
        spAtk = findViewById(R.id.stat_spAtk);
        spDfs = findViewById(R.id.stat_spDfs);
        speed = findViewById(R.id.stat_speed);

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

        getApi = retrofit.create(PokeAPI.class);

        Call<PokemonClass> call1 = getApi.getPokeDetails(id);
        call1.enqueue(new Callback<PokemonClass>() {
            @Override
            public void onResponse(Call<PokemonClass> call, Response<PokemonClass> response) {
                if(!response.isSuccessful()) {
                    return;
                }

                pClass = response.body();

                ID = pClass.id;
                exp = pClass.base_experience;
                height = (float) pClass.height/10;
                weight = (float) pClass.weight/10;

                if (ID < 10){
                    tv_id.setText("#00" + ID);
                }
                else if (ID < 100){
                    tv_id.setText("#0" + ID);
                }
                else
                    tv_id.setText("#" + ID);
                tv_exp.setText(exp + "");
                tv_height.setText(height + " m");
                tv_weight.setText(weight + " kg");

                typeS = "";
                for(TypeClass t: pClass.types){
                    assert t.type.getName() != null;
                    typeS += t.type.getName().substring(0, 1).toUpperCase() + t.type.getName().substring(1).toLowerCase() + "\n";
                }

                abilityS = "";
                for(Ability a : pClass.abilities){
                    abilityS += a.ability.getName().substring(0, 1).toUpperCase() + a.ability.getName().substring(1).toLowerCase() + "\n";
                }

                for (Stats s : pClass.stats){
                    if(s.stat.getName().equals("hp"))
                        HP =  s.stat.getName().toUpperCase() + " : " + s.base_stat + "\n";
                    else if (s.stat.getName().equals("attack"))
                        Attack = s.stat.getName().substring(0,1).toUpperCase() + s.stat.getName().substring(1).toLowerCase()
                                + " : " + s.base_stat + "\n";
                    else if (s.stat.getName().equals("defense"))
                        Defense = s.stat.getName().substring(0,1).toUpperCase() + s.stat.getName().substring(1).toLowerCase()
                                + " : " + s.base_stat + "\n";
                    else if (s.stat.getName().equals("special-attack"))
                        SpAtk = s.stat.getName().substring(0,1).toUpperCase() + s.stat.getName().substring(1).toLowerCase()
                                + " : " + s.base_stat + "\n";
                    else if (s.stat.getName().equals("special-defense"))
                        SpDfs = s.stat.getName().substring(0,1).toUpperCase() + s.stat.getName().substring(1).toLowerCase()
                                + " : " + s.base_stat + "\n";
                    else if (s.stat.getName().equals("speed"))
                        Speed = s.stat.getName().substring(0,1).toUpperCase() + s.stat.getName().substring(1).toLowerCase()
                                + " : " + s.base_stat + "\n";
                }

                tv_type.setText(typeS);
                tv_ability.setText(abilityS);
                hp.setText(HP);
                attack.setText(Attack);
                defense.setText(Defense);
                spAtk.setText(SpAtk);
                spDfs.setText(SpDfs);
                speed.setText(Speed);
            }

            @Override
            public void onFailure(Call<PokemonClass> call, Throwable t) {
                Log.i("Error:", "Fetch: "+ t.getMessage());
            }
        });

    }
}
