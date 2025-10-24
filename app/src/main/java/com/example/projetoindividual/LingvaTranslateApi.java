package com.example.projetoindividual;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LingvaTranslateApi {

    @GET("{source}/{target}/{text}")
    Call<TranslationResponse> traduzir(
            @Path("source") String source,
            @Path("target") String target,
            @Path("text") String text
    );

    class Service {
        private static final String BASE_URL = "https://lingva.ml/api/v1/";
        private static LingvaTranslateApi instance;

        public static LingvaTranslateApi getInstance() {
            if (instance == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                instance = retrofit.create(LingvaTranslateApi.class);
            }
            return instance;
        }
    }

    class TranslationResponse {
        private String translation;

        public String getTranslation() {
            return translation;
        }
    }
}