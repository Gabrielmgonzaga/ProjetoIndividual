package com.example.projetoindividual;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private Button btnBuscar, btnFavoritos;
    private RecyclerView recyclerView;
    private EditText edtEntrada;
    private ArrayList<Pais> paises;
    private Retrofit retrofit;
    private Adapter adapter;
    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        radioGroup = findViewById(R.id.radioGroup);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnFavoritos = findViewById(R.id.btnFavoritos);
        recyclerView = findViewById(R.id.recyclerView);
        edtEntrada = findViewById(R.id.edtEntrada);
        paises = new ArrayList<>();
        preferences = new Preferences(this);

        String url = "https://restcountries.com/v3.1/";
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(paises);
        recyclerView.setAdapter(adapter);

        btnBuscar.setOnClickListener(view -> {
            if (!edtEntrada.getText().toString().isEmpty()) {
                String entrada = edtEntrada.getText().toString();

                if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonNome) {
                    traduzirEBuscar(entrada, true);
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonRegiao) {
                    traduzirEBuscar(entrada, false);
                }
            } else {
                Toast.makeText(MainActivity.this, "Informe o nome ou continente", Toast.LENGTH_SHORT).show();
            }
        });

        btnFavoritos.setOnClickListener(view -> mostrarFavoritos());
    }

    private void traduzirEBuscar(String texto, boolean porNome) {
        LingvaTranslateApi api = LingvaTranslateApi.Service.getInstance();
        Call<LingvaTranslateApi.TranslationResponse> call = api.traduzir("pt", "en", texto);

        call.enqueue(new Callback<LingvaTranslateApi.TranslationResponse>() {
            @Override
            public void onResponse(Call<LingvaTranslateApi.TranslationResponse> call, Response<LingvaTranslateApi.TranslationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String traduzido = response.body().getTranslation();
                    if (porNome) {
                        buscarPorNome(traduzido);
                    } else {
                        buscarPorRegiao(traduzido);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Erro ao traduzir texto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LingvaTranslateApi.TranslationResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Falha na tradução", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buscarPorNome(String nome) {
        RestCountriesApi api = retrofit.create(RestCountriesApi.class);
        Call<List<Pais>> call = api.buscarPorNome(nome);

        call.enqueue(new Callback<List<Pais>>() {
            @Override
            public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    paises.clear();
                    paises.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "País não encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pais>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erro ao buscar país", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buscarPorRegiao(String regiao) {
        RestCountriesApi api = retrofit.create(RestCountriesApi.class);
        Call<List<Pais>> call = api.buscarPorRegiao(regiao);

        call.enqueue(new Callback<List<Pais>>() {
            @Override
            public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    paises.clear();
                    paises.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Nenhum país encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pais>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erro ao buscar países", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarFavoritos() {
        Set<String> favoritos = preferences.getFavoritos();

        if (favoritos.isEmpty()) {
            Toast.makeText(this, "Nenhum país favorito", Toast.LENGTH_SHORT).show();
            paises.clear();
            adapter.notifyDataSetChanged();
            return;
        }

        paises.clear();
        RestCountriesApi api = retrofit.create(RestCountriesApi.class);

        for (String nome : favoritos) {
            Call<List<Pais>> call = api.buscarPorNome(nome);
            call.enqueue(new Callback<List<Pais>>() {
                @Override
                public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                        paises.addAll(response.body());
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<Pais>> call, Throwable t) {
                }
            });
        }
    }
}