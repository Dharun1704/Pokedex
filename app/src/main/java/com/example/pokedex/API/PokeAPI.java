package com.example.pokedex.API;

import com.example.pokedex.PokeList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PokeAPI {
    @GET("pokemon?offset=0&limit=964")
    Call<PokeList> getPokeJson();
}
