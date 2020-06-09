package com.example.pokedex;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PokeAPI {
    @GET("pokemon")
    Call<PokeList> getPokeJson();
}
