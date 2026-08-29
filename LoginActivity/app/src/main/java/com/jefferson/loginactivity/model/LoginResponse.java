package com.jefferson.loginactivity.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("sucesso")
    private boolean sucesso;

    @SerializedName("mensagem")
    private String mensagem;

    @SerializedName("responsavel")
    private Responsavel responsavel;


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


    public Responsavel getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Responsavel responsavel) {
        this.responsavel = responsavel;
    }


    // ============================================================
    // RESPONSÁVEL
    // ============================================================

    public static class Responsavel {

        @SerializedName("id")
        private int id;

        @SerializedName("nome")
        private String nome;

        @SerializedName("cpf")
        private String cpf;

        @SerializedName("telefone")
        private String telefone;

        @SerializedName("email")
        private String email;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }


        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }


        public String getCpf() {
            return cpf;
        }

        public void setCpf(String cpf) {
            this.cpf = cpf;
        }


        public String getTelefone() {
            return telefone;
        }

        public void setTelefone(String telefone) {
            this.telefone = telefone;
        }


        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}