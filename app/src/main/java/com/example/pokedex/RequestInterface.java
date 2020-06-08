package com.example.pokedex;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestInterface {
    @GET("api/v2/pokemon")
    Call<List<Pokemon>> getPokeJson();
}
