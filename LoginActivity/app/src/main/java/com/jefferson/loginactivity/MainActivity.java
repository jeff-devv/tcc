package com.jefferson.loginactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.jefferson.loginactivity.api.ApiClient;
import com.jefferson.loginactivity.model.LoginResponse;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;

    private Button btnEntrar;
    private Button btnCadastrar;
    private Button btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        initComponents();

        // =====================================================
        // BOTÃO CADASTRAR
        // =====================================================

        btnCadastrar.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            CadastroActivity.class
                    );

            startActivity(intent);
        });


        // =====================================================
        // BOTÃO ENTRAR
        // =====================================================

        btnEntrar.setOnClickListener(v -> {

            fazerLogin();
        });


        // =====================================================
        // BOTÃO VOLTAR
        // =====================================================

        btnVoltar.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            Inicio.class
                    );

            startActivity(intent);

            finish();
        });


        // =====================================================
        // WINDOW INSETS
        // =====================================================

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );
    }


    // =========================================================
    // INICIALIZAR COMPONENTES
    // =========================================================

    private void initComponents() {

        edtEmail =
                findViewById(R.id.edtEmail);

        edtSenha =
                findViewById(R.id.edtSenha);

        btnCadastrar =
                findViewById(R.id.btnCadastrar);

        btnEntrar =
                findViewById(R.id.btnEntrarResponsaveis);

        btnVoltar =
                findViewById(R.id.Voltara);
    }


    // =========================================================
    // LOGIN
    // =========================================================

    private void fazerLogin() {

        String email =
                edtEmail
                        .getText()
                        .toString()
                        .trim();

        String senha =
                edtSenha
                        .getText()
                        .toString()
                        .trim();


        // =====================================================
        // VALIDAÇÃO
        // =====================================================

        if (email.isEmpty()) {

            edtEmail.setError(
                    "Digite seu e-mail"
            );

            edtEmail.requestFocus();

            return;
        }


        if (senha.isEmpty()) {

            edtSenha.setError(
                    "Digite sua senha"
            );

            edtSenha.requestFocus();

            return;
        }


        // =====================================================
        // DESABILITA BOTÃO
        // =====================================================

        btnEntrar.setEnabled(false);


        Toast.makeText(
                this,
                "Entrando...",
                Toast.LENGTH_SHORT
        ).show();


        // =====================================================
        // REQUEST BODY
        // =====================================================

        RequestBody emailBody =
                RequestBody.create(
                        MediaType.parse("text/plain"),
                        email
                );


        RequestBody senhaBody =
                RequestBody.create(
                        MediaType.parse("text/plain"),
                        senha
                );


        // =====================================================
        // CHAMADA DA API
        // =====================================================

        Call<LoginResponse> call =
                ApiClient
                        .getApi()
                        .loginResponsavel(
                                ApiClient.API_KEY,
                                emailBody,
                                senhaBody
                        );


        // =====================================================
        // EXECUTA
        // =====================================================

        call.enqueue(
                new Callback<LoginResponse>() {

                    @Override
                    public void onResponse(
                            Call<LoginResponse> call,
                            Response<LoginResponse> response
                    ) {

                        btnEntrar.setEnabled(true);


                        // =========================================
                        // RESPOSTA OK
                        // =========================================

                        if (
                                response.isSuccessful()
                                        &&
                                        response.body() != null
                        ) {

                            LoginResponse resultado =
                                    response.body();


                            if (
                                    resultado.isSucesso()
                            ) {

                                Toast.makeText(
                                        MainActivity.this,
                                        "Login realizado com sucesso!",
                                        Toast.LENGTH_SHORT
                                ).show();


                                // =================================
                                // ABRE HOME
                                // =================================

                                Intent intent =
                                        new Intent(
                                                MainActivity.this,
                                                HomeActivity.class
                                        );


                                // =================================
                                // ENVIA ID DO RESPONSÁVEL
                                // =================================

                                if (
                                        resultado.getResponsavel() != null
                                ) {

                                    intent.putExtra(
                                            "responsavel_id",
                                            resultado
                                                    .getResponsavel()
                                                    .getId()
                                    );
                                }


                                startActivity(intent);


                                finish();

                            } else {

                                Toast.makeText(
                                        MainActivity.this,

                                        resultado.getMensagem(),

                                        Toast.LENGTH_LONG
                                ).show();
                            }

                        } else {

                            Toast.makeText(
                                    MainActivity.this,

                                    "E-mail ou senha inválidos.",

                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }


                    // =============================================
                    // ERRO DE CONEXÃO
                    // =============================================

                    @Override
                    public void onFailure(
                            Call<LoginResponse> call,
                            Throwable t
                    ) {

                        btnEntrar.setEnabled(true);


                        Toast.makeText(
                                MainActivity.this,

                                "Erro de conexão com o servidor:\n"
                                        + t.getMessage(),

                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }
}