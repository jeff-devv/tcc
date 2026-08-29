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

public class HomeMotoristaActivity extends AppCompatActivity {

    CardView listaAlunos, reconhecimentoFaci, entradaAluno, saidaAluno,rotaDia, perfil;

    Button sair;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_motorista);

        initComponents();

        listaAlunos.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListaAlunosActivity.class);
            startActivity(intent);
        });

        reconhecimentoFaci.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReconhecimentoFacialActivity.class);
            startActivity(intent);
        });

        entradaAluno.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistroEntradaActivity.class);
            startActivity(intent);
        });

        saidaAluno.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistroSaidaActivity.class);
            startActivity(intent);
        });

        rotaDia.setOnClickListener(v -> {
            Intent intent = new Intent(this, RotaDiaActivity.class);
            startActivity(intent);
        });

        perfil.setOnClickListener(v -> {
            Intent intent = new Intent(this, PerfilMotoristaActivity.class);
            startActivity(intent);
        });

        sair.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginMotoristaActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initComponents() {
        listaAlunos = findViewById(R.id.cardLista);
        reconhecimentoFaci = findViewById(R.id.cardFacial);
        entradaAluno = findViewById(R.id.cardEntrada);
        saidaAluno = findViewById(R.id.cardSaida);
        rotaDia = findViewById(R.id.cardRota);
        perfil = findViewById(R.id.cardPerfil);
        sair = findViewById(R.id.btnSair);
    }
}