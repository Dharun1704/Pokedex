package com.example.pokedex.Interface;

import com.example.pokedex.Model.PokeDetail;
import com.example.pokedex.Model.PokeList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PokeAPI {
    @GET("pokemon?offset=0&limit=964")
    Call<PokeList> getPokeNameJson();

}
