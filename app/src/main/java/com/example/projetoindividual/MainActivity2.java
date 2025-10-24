package com.example.projetoindividual;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends AppCompatActivity {

    private ImageView imgBandeira;
    private TextView txtNome, txtCapital, txtIdioma, txtRegiao, txtArea, txtPopulacao;
    private Button btnVoltar;
    private CheckBox checkBoxFavorito;
    private Preferences preferences;
    private Pais pais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgBandeira = findViewById(R.id.imgBandeira);
        txtNome = findViewById(R.id.txtNome);
        txtCapital = findViewById(R.id.txtCapital);
        txtIdioma = findViewById(R.id.txtIdioma);
        txtRegiao = findViewById(R.id.txtRegiao);
        txtArea = findViewById(R.id.txtArea);
        txtPopulacao = findViewById(R.id.txtPopulacao);
        btnVoltar = findViewById(R.id.btnVoltar);
        checkBoxFavorito = findViewById(R.id.checkBoxFavorito);
        preferences = new Preferences(this);

        pais = (Pais) getIntent().getSerializableExtra("pais");

        if (pais != null) {
            Glide.with(this).load(pais.getFlags().getPng()).into(imgBandeira);

            NumberFormat nf = NumberFormat.getInstance(new Locale("pt", "BR"));

            String nome = pais.getName().getCommon();
            String capital = pais.getCapital().get(0);
            String idioma = pais.getLanguages().values().iterator().next();
            String regiao = pais.getRegion();

            traduzirEExibir(nome, capital, idioma, regiao, nf);
        }

        btnVoltar.setOnClickListener(v -> finish());
    }

    private void traduzirEExibir(String nome, String capital, String idioma, String regiao, NumberFormat nf) {
        LingvaTranslateApi api = LingvaTranslateApi.Service.getInstance();

        traduzirCampo(api, nome, txtNome, "Nome: ");
        traduzirCampo(api, capital, txtCapital, "Capital: ");
        traduzirCampo(api, idioma, txtIdioma, "Idioma: ");
        traduzirCampo(api, regiao, txtRegiao, "Continente: ");

        txtArea.setText("Área: " + nf.format(pais.getArea()) + " km²");
        txtPopulacao.setText("População: " + nf.format(pais.getPopulation()) + " habitantes");

        checkBoxFavorito.setChecked(preferences.isFavorito(pais.getName().getCommon()));

        checkBoxFavorito.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                preferences.adicionarFavorito(pais.getName().getCommon());
            } else {
                preferences.removerFavorito(pais.getName().getCommon());
            }
        });
    }

    private void traduzirCampo(LingvaTranslateApi api, String textoOriginal, TextView textView, String prefixo) {
        Call<LingvaTranslateApi.TranslationResponse> call = api.traduzir("en", "pt", textoOriginal);
        call.enqueue(new Callback<LingvaTranslateApi.TranslationResponse>() {
            @Override
            public void onResponse(Call<LingvaTranslateApi.TranslationResponse> call, Response<LingvaTranslateApi.TranslationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    textView.setText(prefixo + response.body().getTranslation());
                } else {
                    textView.setText(prefixo + textoOriginal);
                }
            }

            @Override
            public void onFailure(Call<LingvaTranslateApi.TranslationResponse> call, Throwable t) {
                textView.setText(prefixo + textoOriginal);
            }
        });
    }
}