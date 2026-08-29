package com.jefferson.loginactivity.api;

import com.jefferson.loginactivity.model.CadastroResponse;
import com.jefferson.loginactivity.model.LoginResponse;
import com.jefferson.loginactivity.model.ReconhecimentoResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface FacialApi {

    // ============================================================
    // CADASTRO DE RESPONSÁVEL + ALUNO + FOTO
    // ============================================================

    @Multipart
    @POST("api/alunos/cadastrar")
    Call<CadastroResponse> cadastrarAluno(

            @Header("X-API-Key")
            String apiKey,

            @Part("nome_responsavel")
            RequestBody nomeResponsavel,

            @Part("cpf")
            RequestBody cpf,

            @Part("telefone")
            RequestBody telefone,

            @Part("email")
            RequestBody email,

            @Part("senha")
            RequestBody senha,

            @Part("nome")
            RequestBody nome,

            @Part("matricula")
            RequestBody matricula,

            @Part("escola")
            RequestBody escola,

            @Part("turma")
            RequestBody turma,

            @Part
            MultipartBody.Part foto
    );


    // ============================================================
    // LOGIN
    // ============================================================

    @Multipart
    @POST("api/responsaveis/login")
    Call<LoginResponse> loginResponsavel(

            @Header("X-API-Key")
            String apiKey,

            @Part("email")
            RequestBody email,

            @Part("senha")
            RequestBody senha
    );


    // ============================================================
    // BUSCAR RESPONSÁVEL
    // ============================================================

    @GET("api/responsaveis/{responsavel_id}")
    Call<LoginResponse> obterResponsavel(

            @Header("X-API-Key")
            String apiKey,

            @Path("responsavel_id")
            int responsavelId
    );


    // ============================================================
    // RECONHECIMENTO FACIAL
    // ============================================================

    @Multipart
    @POST("api/reconhecimento")
    Call<ReconhecimentoResponse> reconhecer(

            @Header("X-API-Key")
            String apiKey,

            @Part
            MultipartBody.Part foto,

            @Part("registrar_presenca")
            RequestBody registrarPresenca
    );
}