package com.jefferson.loginactivity.model;

import com.google.gson.annotations.SerializedName;

public class CadastroResponse {

    @SerializedName("sucesso")
    private boolean sucesso;

    @SerializedName("mensagem")
    private String mensagem;

    @SerializedName("responsavel_id")
    private Integer responsavelId;

    @SerializedName("aluno_id")
    private Integer alunoId;

    @SerializedName("matricula")
    private String matricula;


    public boolean isSucesso() {
        return sucesso;
    }

    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
    }


    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }


    public Integer getResponsavelId() {
        return responsavelId;
    }

    public void setResponsavelId(Integer responsavelId) {
        this.responsavelId = responsavelId;
    }


    public Integer getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Integer alunoId) {
        this.alunoId = alunoId;
    }


    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}