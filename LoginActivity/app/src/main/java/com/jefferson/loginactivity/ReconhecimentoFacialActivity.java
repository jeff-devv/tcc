package com.jefferson.loginactivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;

import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import com.jefferson.loginactivity.api.ApiClient;
import com.jefferson.loginactivity.model.ReconhecimentoResponse;

import java.io.File;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReconhecimentoFacialActivity
        extends AppCompatActivity {

    private static final String TAG =
            "RECONHECIMENTO";

    // ============================================================
    // COMPONENTES
    // ============================================================

    private PreviewView previewView;

    private TextView txtNomeAluno;
    private TextView txtTurmaAluno;
    private TextView txtStatusReconhecimento;

    private Button btnCapturar;
    private Button btnConfirmarEntrada;
    private Button btnConfirmarSaida;
    private Button btnVoltarHome;

    // ============================================================
    // CAMERA
    // ============================================================

    private ImageCapture imageCapture;

    private boolean cameraPronta = false;

    private boolean processandoFoto = false;

    // ============================================================
    // ALUNO RECONHECIDO
    // ============================================================

    private int alunoId = -1;

    private boolean alunoReconhecido = false;

    // ============================================================
    // PERMISSÃO
    // ============================================================

    private final ActivityResultLauncher<String>
            permissaoCameraLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    concedida -> {

                        if (concedida) {

                            iniciarCamera();

                        } else {

                            Toast.makeText(
                                    this,
                                    "Permissão da câmera é necessária.",
                                    Toast.LENGTH_LONG
                            ).show();

                            txtStatusReconhecimento.setText(
                                    "Permissão da câmera negada."
                            );
                        }
                    }
            );


    // ============================================================
    // ON CREATE
    // ============================================================

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(
                R.layout.activity_reconhecimento_facial
        );

        inicializarComponentes();


        // ========================================================
        // CAPTURAR
        // ========================================================

        btnCapturar.setOnClickListener(
                v -> capturarEReconhecer()
        );


        // ========================================================
        // ENTRADA
        // ========================================================

        btnConfirmarEntrada.setOnClickListener(
                v -> confirmarEntrada()
        );


        // ========================================================
        // SAÍDA
        // ========================================================

        btnConfirmarSaida.setOnClickListener(
                v -> confirmarSaida()
        );


        // ========================================================
        // VOLTAR
        // ========================================================

        btnVoltarHome.setOnClickListener(
                v -> finish()
        );


        // ========================================================
        // CAMERA
        // ========================================================

        if (
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                )
                        == PackageManager.PERMISSION_GRANTED
        ) {

            iniciarCamera();

        } else {

            permissaoCameraLauncher.launch(
                    Manifest.permission.CAMERA
            );
        }
    }


    // ============================================================
    // COMPONENTES
    // ============================================================

    private void inicializarComponentes() {

        previewView =
                findViewById(
                        R.id.previewView
                );

        txtNomeAluno =
                findViewById(
                        R.id.txtNomeAluno
                );

        txtTurmaAluno =
                findViewById(
                        R.id.txtTurmaAluno
                );

        txtStatusReconhecimento =
                findViewById(
                        R.id.txtStatusReconhecimento
                );

        btnCapturar =
                findViewById(
                        R.id.btnCapturar
                );

        btnConfirmarEntrada =
                findViewById(
                        R.id.btnConfirmarEntrada
                );

        btnConfirmarSaida =
                findViewById(
                        R.id.btnConfirmarSaida
                );

        btnVoltarHome =
                findViewById(
                        R.id.btn_voltar_home
                );


        txtNomeAluno.setText(
                "Nenhum aluno reconhecido"
        );

        txtTurmaAluno.setText("");

        txtStatusReconhecimento.setText(
                "Aproxime o rosto e toque em Capturar."
        );


        btnConfirmarEntrada.setEnabled(
                false
        );

        btnConfirmarSaida.setEnabled(
                false
        );
    }


    // ============================================================
    // CAMERA
    // ============================================================

    private void iniciarCamera() {

        if (previewView == null) {
            return;
        }

        previewView.setVisibility(
                View.VISIBLE
        );

        ListenableFuture<ProcessCameraProvider>
                cameraProviderFuture =
                ProcessCameraProvider.getInstance(
                        this
                );

        cameraProviderFuture.addListener(
                () -> {

                    try {

                        ProcessCameraProvider
                                cameraProvider =
                                cameraProviderFuture.get();


                        Preview preview =
                                new Preview.Builder()
                                        .build();


                        preview.setSurfaceProvider(
                                previewView
                                        .getSurfaceProvider()
                        );


                        imageCapture =
                                new ImageCapture.Builder()
                                        .setCaptureMode(
                                                ImageCapture
                                                        .CAPTURE_MODE_MAXIMIZE_QUALITY
                                        )
                                        .build();


                        CameraSelector frontal =
                                CameraSelector
                                        .DEFAULT_FRONT_CAMERA;


                        CameraSelector selecionada;


                        if (
                                cameraProvider.hasCamera(
                                        frontal
                                )
                        ) {

                            selecionada =
                                    frontal;

                        } else {

                            selecionada =
                                    CameraSelector
                                            .DEFAULT_BACK_CAMERA;
                        }


                        cameraProvider.unbindAll();


                        Camera camera =
                                cameraProvider.bindToLifecycle(
                                        this,
                                        selecionada,
                                        preview,
                                        imageCapture
                                );


                        cameraPronta = true;


                        Log.d(
                                TAG,
                                "Câmera pronta: "
                                        + camera
                        );


                        txtStatusReconhecimento.setText(
                                "Câmera ativa. Aproxime o rosto."
                        );


                    } catch (Exception e) {

                        cameraPronta = false;

                        imageCapture = null;

                        Log.e(
                                TAG,
                                "Erro ao abrir câmera.",
                                e
                        );


                        txtStatusReconhecimento.setText(
                                "Erro ao iniciar câmera."
                        );
                    }

                },
                ContextCompat.getMainExecutor(this)
        );
    }


    // ============================================================
    // CAPTURA
    // ============================================================

    private void capturarEReconhecer() {

        if (!cameraPronta) {

            Toast.makeText(
                    this,
                    "A câmera ainda não está pronta.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        if (imageCapture == null) {
            return;
        }


        if (processandoFoto) {
            return;
        }


        processandoFoto = true;


        alunoReconhecido = false;

        alunoId = -1;


        btnConfirmarEntrada.setEnabled(
                false
        );

        btnConfirmarSaida.setEnabled(
                false
        );


        txtStatusReconhecimento.setText(
                "Capturando rosto..."
        );


        File arquivo =
                new File(
                        getCacheDir(),
                        "reconhecimento_"
                                + System.currentTimeMillis()
                                + ".jpg"
                );


        ImageCapture.OutputFileOptions options =
                new ImageCapture
                        .OutputFileOptions
                        .Builder(arquivo)
                        .build();


        imageCapture.takePicture(
                options,
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {

                    @Override
                    public void onImageSaved(
                            @NonNull ImageCapture.OutputFileResults outputFileResults
                    ) {

                        txtStatusReconhecimento.setText(
                                "Foto capturada. Reconhecendo..."
                        );


                        enviarParaReconhecimento(
                                arquivo
                        );
                    }


                    @Override
                    public void onError(
                            @NonNull ImageCaptureException exception
                    ) {

                        processandoFoto = false;


                        txtStatusReconhecimento.setText(
                                "Erro ao capturar rosto."
                        );


                        Toast.makeText(
                                ReconhecimentoFacialActivity.this,
                                "Erro: "
                                        + exception.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }


    // ============================================================
    // ENVIA FOTO
    // ============================================================

    private void enviarParaReconhecimento(
            File foto
    ) {

        RequestBody fotoBody =
                RequestBody.create(
                        MediaType.parse("image/jpeg"),
                        foto
                );


        MultipartBody.Part fotoPart =
                MultipartBody.Part.createFormData(
                        "foto",
                        foto.getName(),
                        fotoBody
                );


        // ========================================================
        // NÃO REGISTRA PRESENÇA AUTOMATICAMENTE
        // ========================================================

        RequestBody registrarPresenca =
                RequestBody.create(
                        MediaType.parse("text/plain"),
                        "false"
                );


        Call<ReconhecimentoResponse> call =
                ApiClient
                        .getApi()
                        .reconhecer(
                                ApiClient.API_KEY,
                                fotoPart,
                                registrarPresenca
                        );


        call.enqueue(
                new Callback<ReconhecimentoResponse>() {

                    @Override
                    public void onResponse(
                            @NonNull Call<ReconhecimentoResponse> call,
                            @NonNull Response<ReconhecimentoResponse> response
                    ) {

                        processandoFoto = false;


                        if (
                                response.isSuccessful()
                                        && response.body() != null
                        ) {

                            ReconhecimentoResponse resultado =
                                    response.body();


                            processarResultado(
                                    resultado
                            );


                        } else {

                            txtStatusReconhecimento.setText(
                                    "Erro na API. Código: "
                                            + response.code()
                            );
                        }


                        apagarFoto(
                                foto
                        );
                    }


                    @Override
                    public void onFailure(
                            @NonNull Call<ReconhecimentoResponse> call,
                            @NonNull Throwable t
                    ) {

                        processandoFoto = false;


                        Log.e(
                                TAG,
                                "Falha na API.",
                                t
                        );


                        txtStatusReconhecimento.setText(
                                "Falha de conexão com o servidor."
                        );


                        apagarFoto(
                                foto
                        );
                    }
                }
        );
    }


    // ============================================================
    // PROCESSAR RESULTADO
    // ============================================================

    private void processarResultado(
            ReconhecimentoResponse resultado
    ) {

        if (
                !resultado.isReconhecido()
                        || resultado.getAluno() == null
        ) {

            alunoReconhecido = false;

            alunoId = -1;


            txtNomeAluno.setText(
                    "Aluno não cadastrado"
            );


            txtTurmaAluno.setText(
                    ""
            );


            txtStatusReconhecimento.setText(
                    resultado.getMensagem()
            );


            btnConfirmarEntrada.setEnabled(
                    false
            );

            btnConfirmarSaida.setEnabled(
                    false
            );


            return;
        }


        // ========================================================
        // ALUNO ENCONTRADO
        // ========================================================

        ReconhecimentoResponse.Aluno aluno =
                resultado.getAluno();


        alunoReconhecido = true;

        alunoId =
                aluno.getId();


        txtNomeAluno.setText(
                aluno.getNome()
        );


        String turma =
                aluno.getTurma() == null
                        ? ""
                        : aluno.getTurma();


        String matricula =
                aluno.getMatricula() == null
                        ? ""
                        : aluno.getMatricula();


        txtTurmaAluno.setText(
                "Turma: "
                        + turma
                        + "\nMatrícula: "
                        + matricula
        );


        String confianca =
                String.format(
                        Locale.getDefault(),
                        "%.1f%%",
                        aluno.getConfianca() * 100
                );


        txtStatusReconhecimento.setText(
                "✓ Rosto reconhecido\n"
                        + "Confiança: "
                        + confianca
        );


        btnConfirmarEntrada.setEnabled(
                true
        );

        btnConfirmarSaida.setEnabled(
                true
        );


        Log.d(
                TAG,
                "Aluno reconhecido: "
                        + aluno.getNome()
        );
    }


    // ============================================================
    // ENTRADA
    // ============================================================

    private void confirmarEntrada() {

        if (!alunoReconhecido) {

            Toast.makeText(
                    this,
                    "Nenhum aluno reconhecido.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        Toast.makeText(
                this,
                "Entrada confirmada!",
                Toast.LENGTH_LONG
        ).show();


        txtStatusReconhecimento.setText(
                "✓ Entrada confirmada para "
                        + txtNomeAluno.getText()
        );
    }


    // ============================================================
    // SAÍDA
    // ============================================================

    private void confirmarSaida() {

        if (!alunoReconhecido) {

            Toast.makeText(
                    this,
                    "Nenhum aluno reconhecido.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        Toast.makeText(
                this,
                "Saída confirmada!",
                Toast.LENGTH_LONG
        ).show();


        txtStatusReconhecimento.setText(
                "✓ Saída confirmada para "
                        + txtNomeAluno.getText()
        );
    }


    // ============================================================
    // APAGA FOTO TEMPORÁRIA
    // ============================================================

    private void apagarFoto(
            File foto
    ) {

        if (
                foto != null
                        && foto.exists()
        ) {

            foto.delete();
        }
    }


    // ============================================================
    // DESTROY
    // ============================================================

    @Override
    protected void onDestroy() {

        cameraPronta = false;

        processandoFoto = false;

        imageCapture = null;

        super.onDestroy();
    }
}