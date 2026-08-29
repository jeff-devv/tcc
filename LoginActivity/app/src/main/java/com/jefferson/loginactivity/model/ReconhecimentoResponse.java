package com.jefferson.loginactivity.model;

import com.google.gson.annotations.SerializedName;

public class ReconhecimentoResponse {

    @SerializedName("reconhecido")
    private boolean reconhecido;

    @SerializedName("mensagem")
    private String mensagem;

    @SerializedName("aluno")
    private Aluno aluno;


    public boolean isReconhecido() {
        return reconhecido;
    }

    public void setReconhecido(boolean reconhecido) {
        this.reconhecido = reconhecido;
    }


    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }


    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }


    // ============================================================
    // ALUNO
    // ============================================================

    public static class Aluno {

        @SerializedName("id")
        private int id;

        @SerializedName("nome")
        private String nome;

        @SerializedName("matricula")
        private String matricula;

        @SerializedName("escola")
        private String escola;

        @SerializedName("turma")
        private String turma;

        @SerializedName("confianca")
        private double confianca;


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


        public String getMatricula() {
            return matricula;
        }

        public void setMatricula(String matricula) {
            this.matricula = matricula;
        }


        public String getEscola() {
            return escola;
        }

        public void setEscola(String escola) {
            this.escola = escola;
        }


        public String getTurma() {
            return turma;
        }

        public void setTurma(String turma) {
            this.turma = turma;
        }


        public double getConfianca() {
            return confianca;
        }

        public void setConfianca(double confianca) {
            this.confianca = confianca;
        }
    }
}