package com.jefferson.loginactivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.jefferson.loginactivity.api.ApiClient;
import com.jefferson.loginactivity.model.CadastroResponse;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroActivity extends AppCompatActivity {

    private static final String TAG = "CADASTRO";

    // ============================================================
    // BOTÕES
    // ============================================================

    private Button btnCadastrar;
    private Button btnVoltar;
    private Button btnCapturarFoto;

    // ============================================================
    // RESPONSÁVEL
    // ============================================================

    private EditText edtNomeResponsavel;
    private EditText edtCpf;
    private EditText edtTelefone;
    private EditText edtEmail;
    private EditText edtSenha;
    private EditText edtConfirmarSenha;

    // ============================================================
    // ALUNO
    // ============================================================

    private EditText edtNomeAluno;
    private EditText edtMatricula;
    private EditText edtEscola;
    private EditText edtTurma;

    // ============================================================
    // STATUS
    // ============================================================

    private TextView txtStatusFoto;

    // ============================================================
    // CAMERA
    // ============================================================

    private PreviewView previewView;

    private ImageCapture imageCapture;

    private boolean cameraPronta = false;

    private boolean processandoCadastro = false;

    // ============================================================
    // FOTO
    // ============================================================

    private File fotoAluno;

    // ============================================================
    // PERMISSÃO
    // ============================================================

    private final ActivityResultLauncher<String> permissaoCameraLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    concedida -> {

                        if (concedida) {

                            Log.d(
                                    TAG,
                                    "Permissão da câmera concedida."
                            );

                            iniciarCamera();

                        } else {

                            Toast.makeText(
                                    this,
                                    "Permissão da câmera é necessária para tirar a foto.",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
            );


    // ============================================================
    // ON CREATE
    // ============================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(
                R.layout.activity_cadastro
        );

        inicializarComponentes();

        // ========================================================
        // VOLTAR
        // ========================================================

        btnVoltar.setOnClickListener(
                v -> finish()
        );

        // ========================================================
        // TIRAR FOTO
        // ========================================================

        btnCapturarFoto.setOnClickListener(
                v -> tirarFoto()
        );

        // ========================================================
        // CADASTRAR
        // ========================================================

        btnCadastrar.setOnClickListener(
                v -> validarECadastrar()
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

        // ========================================================
        // INSETS
        // ========================================================

        View mainView =
                findViewById(R.id.main);

        if (mainView != null) {

            ViewCompat.setOnApplyWindowInsetsListener(
                    mainView,
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
    }


    // ============================================================
    // INICIALIZAR COMPONENTES
    // ============================================================

    private void inicializarComponentes() {

        btnCadastrar =
                findViewById(R.id.btn_Cadastrar);

        btnVoltar =
                findViewById(R.id.btnvolar);

        btnCapturarFoto =
                findViewById(R.id.btnCapturarFoto);

        txtStatusFoto =
                findViewById(R.id.txtStatusFoto);

        // --------------------------------------------------------
        // RESPONSÁVEL
        // --------------------------------------------------------

        edtNomeResponsavel =
                findViewById(R.id.edtNome);

        edtCpf =
                findViewById(R.id.edtCpf);

        edtTelefone =
                findViewById(R.id.edtTelefone);

        edtEmail =
                findViewById(R.id.edtEmail);

        edtSenha =
                findViewById(R.id.edtSenha);

        edtConfirmarSenha =
                findViewById(R.id.edtConfirmarSenha);

        // --------------------------------------------------------
        // ALUNO
        // --------------------------------------------------------

        edtNomeAluno =
                findViewById(R.id.edtNomeAluno);

        edtMatricula =
                findViewById(R.id.edtMatricula);

        edtEscola =
                findViewById(R.id.edtEscola);

        edtTurma =
                findViewById(R.id.edtTurma);

        // --------------------------------------------------------
        // CAMERA
        // --------------------------------------------------------

        previewView =
                findViewById(R.id.previewView);

        if (previewView == null) {

            Log.e(
                    TAG,
                    "previewView não encontrado."
            );
        }
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
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(
                () -> {

                    try {

                        ProcessCameraProvider cameraProvider =
                                cameraProviderFuture.get();

                        // ------------------------------------------------
                        // PREVIEW
                        // ------------------------------------------------

                        Preview preview =
                                new Preview.Builder()
                                        .build();

                        preview.setSurfaceProvider(
                                previewView.getSurfaceProvider()
                        );

                        // ------------------------------------------------
                        // CAPTURA
                        // ------------------------------------------------

                        imageCapture =
                                new ImageCapture.Builder()
                                        .setCaptureMode(
                                                ImageCapture
                                                        .CAPTURE_MODE_MAXIMIZE_QUALITY
                                        )
                                        .build();

                        // ------------------------------------------------
                        // CAMERA FRONTAL
                        // ------------------------------------------------

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

                            CameraSelector traseira =
                                    CameraSelector
                                            .DEFAULT_BACK_CAMERA;

                            if (
                                    cameraProvider.hasCamera(
                                            traseira
                                    )
                            ) {

                                selecionada =
                                        traseira;

                            } else {

                                throw new Exception(
                                        "Nenhuma câmera encontrada."
                                );
                            }
                        }

                        // ------------------------------------------------
                        // LIMPA
                        // ------------------------------------------------

                        cameraProvider.unbindAll();

                        // ------------------------------------------------
                        // LIGA
                        // ------------------------------------------------

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
                                "Câmera pronta: " + camera
                        );

                        if (txtStatusFoto != null) {

                            txtStatusFoto.setText(
                                    "Câmera pronta. Tire a foto do aluno."
                            );
                        }

                    } catch (Exception e) {

                        cameraPronta = false;
                        imageCapture = null;

                        Log.e(
                                TAG,
                                "Erro ao iniciar câmera.",
                                e
                        );

                        Toast.makeText(
                                this,
                                "Erro ao iniciar câmera:\n"
                                        + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }

                },
                ContextCompat.getMainExecutor(this)
        );
    }


    // ============================================================
    // TIRAR FOTO
    // ============================================================

    private void tirarFoto() {

        if (!cameraPronta || imageCapture == null) {

            Toast.makeText(
                    this,
                    "A câmera ainda não está pronta.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        fotoAluno =
                new File(
                        getCacheDir(),
                        "aluno_"
                                + System.currentTimeMillis()
                                + ".jpg"
                );

        ImageCapture.OutputFileOptions options =
                new ImageCapture
                        .OutputFileOptions
                        .Builder(fotoAluno)
                        .build();

        btnCapturarFoto.setEnabled(false);

        imageCapture.takePicture(
                options,
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {

                    @Override
                    public void onImageSaved(
                            @NonNull ImageCapture.OutputFileResults outputFileResults
                    ) {

                        Log.d(
                                TAG,
                                "Foto capturada: "
                                        + fotoAluno.getAbsolutePath()
                        );

                        if (txtStatusFoto != null) {

                            txtStatusFoto.setText(
                                    "Foto capturada com sucesso ✔"
                            );
                        }

                        btnCapturarFoto.setEnabled(true);

                        btnCapturarFoto.setText(
                                "Tirar Foto Novamente"
                        );

                        Toast.makeText(
                                CadastroActivity.this,
                                "Foto capturada!",
                                Toast.LENGTH_SHORT
                        ).show();
                    }


                    @Override
                    public void onError(
                            @NonNull ImageCaptureException exception
                    ) {

                        fotoAluno = null;

                        btnCapturarFoto.setEnabled(true);

                        if (txtStatusFoto != null) {

                            txtStatusFoto.setText(
                                    "Erro ao capturar foto."
                            );
                        }

                        Toast.makeText(
                                CadastroActivity.this,
                                "Erro: "
                                        + exception.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }


    // ============================================================
    // VALIDAR
    // ============================================================

    private void validarECadastrar() {

        String nomeResponsavel =
                edtNomeResponsavel.getText()
                        .toString()
                        .trim();

        String cpf =
                edtCpf.getText()
                        .toString()
                        .trim();

        String telefone =
                edtTelefone.getText()
                        .toString()
                        .trim();

        String email =
                edtEmail.getText()
                        .toString()
                        .trim();

        String senha =
                edtSenha.getText()
                        .toString();

        String confirmarSenha =
                edtConfirmarSenha.getText()
                        .toString();

        String nomeAluno =
                edtNomeAluno.getText()
                        .toString()
                        .trim();

        String matricula =
                edtMatricula.getText()
                        .toString()
                        .trim();

        String escola =
                edtEscola.getText()
                        .toString()
                        .trim();

        String turma =
                edtTurma.getText()
                        .toString()
                        .trim();


        // ========================================================
        // CAMPOS OBRIGATÓRIOS
        // ========================================================

        if (
                nomeResponsavel.isEmpty()
                        || cpf.isEmpty()
                        || telefone.isEmpty()
                        || email.isEmpty()
                        || senha.isEmpty()
                        || confirmarSenha.isEmpty()
                        || nomeAluno.isEmpty()
                        || matricula.isEmpty()
                        || escola.isEmpty()
                        || turma.isEmpty()
        ) {

            Toast.makeText(
                    this,
                    "Preencha todos os campos.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }


        // ========================================================
        // E-MAIL
        // ========================================================

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()) {

            edtEmail.setError(
                    "E-mail inválido"
            );

            edtEmail.requestFocus();

            return;
        }


        // ========================================================
        // SENHAS
        // ========================================================

        if (!senha.equals(confirmarSenha)) {

            edtConfirmarSenha.setError(
                    "As senhas não são iguais."
            );

            edtConfirmarSenha.requestFocus();

            return;
        }


        // ========================================================
        // SENHA MÍNIMA
        // ========================================================

        if (senha.length() < 6) {

            edtSenha.setError(
                    "A senha deve ter pelo menos 6 caracteres."
            );

            edtSenha.requestFocus();

            return;
        }


        // ========================================================
        // FOTO
        // ========================================================

        if (
                fotoAluno == null
                        || !fotoAluno.exists()
                        || fotoAluno.length() == 0
        ) {

            Toast.makeText(
                    this,
                    "Tire a foto do aluno antes de cadastrar.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }


        if (processandoCadastro) {
            return;
        }

        processandoCadastro = true;

        btnCadastrar.setEnabled(false);

        txtStatusFoto.setText(
                "Enviando cadastro..."
        );


        enviarCadastroApi(
                nomeResponsavel,
                cpf,
                telefone,
                email,
                senha,
                nomeAluno,
                matricula,
                escola,
                turma,
                fotoAluno
        );
    }


    // ============================================================
    // ENVIA PARA API
    // ============================================================

    private void enviarCadastroApi(
            String nomeResponsavel,
            String cpf,
            String telefone,
            String email,
            String senha,
            String nomeAluno,
            String matricula,
            String escola,
            String turma,
            File foto
    ) {

        RequestBody nomeResponsavelBody =
                texto(nomeResponsavel);

        RequestBody cpfBody =
                texto(cpf);

        RequestBody telefoneBody =
                texto(telefone);

        RequestBody emailBody =
                texto(email);

        RequestBody senhaBody =
                texto(senha);

        RequestBody nomeAlunoBody =
                texto(nomeAluno);

        RequestBody matriculaBody =
                texto(matricula);

        RequestBody escolaBody =
                texto(escola);

        RequestBody turmaBody =
                texto(turma);


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


        Call<CadastroResponse> call =
                ApiClient
                        .getApi()
                        .cadastrarAluno(

                                ApiClient.API_KEY,

                                nomeResponsavelBody,
                                cpfBody,
                                telefoneBody,
                                emailBody,
                                senhaBody,

                                nomeAlunoBody,
                                matriculaBody,
                                escolaBody,
                                turmaBody,

                                fotoPart
                        );


        call.enqueue(
                new Callback<CadastroResponse>() {

                    @Override
                    public void onResponse(
                            @NonNull Call<CadastroResponse> call,
                            @NonNull Response<CadastroResponse> response
                    ) {

                        processandoCadastro = false;

                        btnCadastrar.setEnabled(true);


                        if (
                                response.isSuccessful()
                                        && response.body() != null
                        ) {

                            CadastroResponse resultado =
                                    response.body();


                            if (resultado.isSucesso()) {

                                Toast.makeText(
                                        CadastroActivity.this,
                                        "Cadastro realizado com sucesso! ✔",
                                        Toast.LENGTH_LONG
                                ).show();


                                if (
                                        foto.exists()
                                ) {

                                    foto.delete();
                                }

                                fotoAluno = null;

                                finish();

                            } else {

                                Toast.makeText(
                                        CadastroActivity.this,
                                        resultado.getMensagem(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }


                        } else {

                            String mensagem =
                                    "Erro no servidor. Código: "
                                            + response.code();


                            try {

                                if (
                                        response.errorBody()
                                                != null
                                ) {

                                    mensagem =
                                            response
                                                    .errorBody()
                                                    .string();
                                }

                            } catch (Exception e) {

                                Log.e(
                                        TAG,
                                        "Erro lendo resposta.",
                                        e
                                );
                            }


                            Toast.makeText(
                                    CadastroActivity.this,
                                    mensagem,
                                    Toast.LENGTH_LONG
                            ).show();
                        }


                        txtStatusFoto.setText(
                                "Cadastro finalizado."
                        );
                    }


                    @Override
                    public void onFailure(
                            @NonNull Call<CadastroResponse> call,
                            @NonNull Throwable t
                    ) {

                        processandoCadastro = false;

                        btnCadastrar.setEnabled(true);

                        Log.e(
                                TAG,
                                "Erro de conexão.",
                                t
                        );

                        Toast.makeText(
                                CadastroActivity.this,
                                "Falha de conexão com a API:\n"
                                        + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                        txtStatusFoto.setText(
                                "Falha ao conectar com o servidor."
                        );
                    }
                }
        );
    }


    // ============================================================
    // TEXTO
    // ============================================================

    private RequestBody texto(String valor) {

        return RequestBody.create(
                MediaType.parse("text/plain"),
                valor
        );
    }


    // ============================================================
    // DESTROY
    // ============================================================

    @Override
    protected void onDestroy() {

        cameraPronta = false;

        imageCapture = null;

        super.onDestroy();
    }
}