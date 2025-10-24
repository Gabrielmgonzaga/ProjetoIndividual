package com.example.projetoindividual;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class Preferences {

    private static final String PREF_NAME = "paises_favoritos";
    private static final String KEY_FAVORITOS = "favoritos";

    private SharedPreferences sharedPreferences;

    public Preferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void adicionarFavorito(String nomePais) {
        Set<String> favoritos = getFavoritos();
        favoritos.add(nomePais);
        sharedPreferences.edit().putStringSet(KEY_FAVORITOS, favoritos).apply();
    }

    public void removerFavorito(String nomePais) {
        Set<String> favoritos = getFavoritos();
        favoritos.remove(nomePais);
        sharedPreferences.edit().putStringSet(KEY_FAVORITOS, favoritos).apply();
    }

    public boolean isFavorito(String nomePais) {
        return getFavoritos().contains(nomePais);
    }

    public Set<String> getFavoritos() {
        return new HashSet<>(sharedPreferences.getStringSet(KEY_FAVORITOS, new HashSet<>()));
    }
}