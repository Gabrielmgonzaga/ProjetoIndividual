package com.example.projetoindividual;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestCountriesApi {

    @GET("name/{name}?fullText=true")
    Call<List<Pais>> buscarPorNome(@Path("name") String name);

    @GET("region/{region}")
    Call<List<Pais>> buscarPorRegiao(@Path("region") String region);
}