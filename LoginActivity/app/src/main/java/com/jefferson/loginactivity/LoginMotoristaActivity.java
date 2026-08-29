package com.jefferson.loginactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginMotoristaActivity extends AppCompatActivity {

    Button btnCadastrar, btnEntrar, btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_motorista);

        initComponents();

        btnEntrar.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeMotoristaActivity.class);
            startActivity(intent);
        });

        btnCadastrar.setOnClickListener(v -> {
            Intent intent = new Intent(this, cadastro_motorista.class);
            startActivity(intent);
        });

        btnVoltar.setOnClickListener(v -> {
            Intent intent = new Intent(this, Inicio.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initComponents() {
        btnCadastrar = findViewById(R.id.btnCadastroMotorista);
        btnEntrar    = findViewById(R.id.btnEntrarMotorista);
        btnVoltar    = findViewById(R.id.voltarm);
    }
}