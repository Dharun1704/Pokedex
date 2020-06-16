package com.example.pokedex.Interface;

import com.example.pokedex.Model.AreaEncounters;
import com.example.pokedex.Model.LocationAreas;
import com.example.pokedex.Model.PokeList;
import com.example.pokedex.Model.PokemonClass;
import com.example.pokedex.Model.RegionLoc;
import com.example.pokedex.Model.TypePokeList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PokeAPI {
    @GET("pokemon/?offset=0&limit=964")
    Call<PokeList> getPokeNameJson();

    @GET("pokemon/{id}/")
    Call<PokemonClass> getPokeDetails(@Path("id") int id);

    @GET("type")
    Call<PokeList> getPokeTypeJson();

    @GET("type/{id}/")
    Call<TypePokeList> getTypePokemon(@Path("id") int id);

    @GET("location?offset=0&limit=781")
    Call<PokeList> getLocationNameJson();

    @GET("item?offset=0&limit=954")
    Call<PokeList> getItemsNameJson();

    @GET("region")
    Call<PokeList> getRegionNameJson();

    @GET("region/{id}/")
    Call<RegionLoc> getRegionLocationJson(@Path("id") int id);

    @GET("location/{id}/")
    Call<LocationAreas> getLocationAreasJson(@Path("id") int id);

    @GET("location-area/{id}")
    Call<AreaEncounters> getPokemonEncounterJson(@Path("id") int id);
}
