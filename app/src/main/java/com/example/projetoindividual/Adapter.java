package com.example.projetoindividual;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private ArrayList<Pais> list;

    public Adapter(ArrayList<Pais> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_itens, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Pais pais = list.get(position);

        // Traduzir o nome do país (de inglês para português)
        String nomeOriginal = pais.getName().getCommon();
        traduzirNome(holder.txtNome, nomeOriginal);

        Glide.with(holder.itemView.getContext())
                .load(pais.getFlags().getPng())
                .into(holder.imgBandeira);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), MainActivity2.class);
            intent.putExtra("pais", pais);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArrayList<Pais> getTodosOsPaises() {
        return list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome;
        ImageView imgBandeira;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txtNome);
            imgBandeira = itemView.findViewById(R.id.imgBandeira);
        }
    }

    // Método auxiliar para traduzir o nome
    private void traduzirNome(TextView textView, String nomeOriginal) {
        LingvaTranslateApi api = LingvaTranslateApi.Service.getInstance();
        Call<LingvaTranslateApi.TranslationResponse> call = api.traduzir("en", "pt", nomeOriginal);

        call.enqueue(new Callback<LingvaTranslateApi.TranslationResponse>() {
            @Override
            public void onResponse(Call<LingvaTranslateApi.TranslationResponse> call, Response<LingvaTranslateApi.TranslationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    textView.setText(response.body().getTranslation());
                } else {
                    textView.setText(nomeOriginal);
                }
            }

            @Override
            public void onFailure(Call<LingvaTranslateApi.TranslationResponse> call, Throwable t) {
                textView.setText(nomeOriginal);
            }
        });
    }
}