package com.jefferson.loginactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {

    CardView perfil, rastreamento, motorista, histórico, pagamento;

    Button sair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        initComponents();

        perfil.setOnClickListener(v -> {
            Intent intent = new Intent(this, PerfilActivity.class);
            startActivity(intent);
        });

        rastreamento.setOnClickListener(v -> {
            Intent intent = new Intent(this, RastreamentoActivity.class);
            startActivity(intent);
        });

        motorista.setOnClickListener(v -> {
            Intent intent = new Intent(this, EscolhaMotoristaActivity.class);
            startActivity(intent);
        });

        histórico.setOnClickListener(v -> {
            Intent intent = new Intent(this, HistoricoActivity.class);
            startActivity(intent);
        });

        pagamento.setOnClickListener(v -> {
            Intent intent = new Intent(this, PagamentoActivity.class);
            startActivity(intent);
        });

        sair.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initComponents() {
        perfil = findViewById(R.id.card_son_profile);
        rastreamento = findViewById(R.id.card_rastreamento);
        motorista = findViewById(R.id.card_motorista);
        histórico = findViewById(R.id.card_historico);
        pagamento = findViewById(R.id.card_pagamentos);
        sair = findViewById(R.id.btnSairP);
    }
}