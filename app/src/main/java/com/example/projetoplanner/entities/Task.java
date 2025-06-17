package com.example.projetoplanner.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    public int id; // <-- MUDANÇA AQUI: Adicione o modificador 'public'
    String descricao;
    Boolean status; // É uma boa prática ter todos os campos no início, ou mantê-los agrupados

    // Construtor vazio é necessário para o Room
    public Task() {}

    // Construtor para criar novas tarefas (sem ID, pois é autoGenerate)
    public Task(String descricao, Boolean status) {
        this.descricao = descricao;
        this.status = status;
    }

    // Getters e Setters
    // Você não precisa de um setter para 'id' se ele for auto-gerado,
    // mas ter um getter público é geralmente útil.
    public int getId() { // Adicione um getter para 'id'
        return id;
    }

    // O setter para ID não é estritamente necessário para o Room se autoGenerate for true,
    // mas pode ser útil para outras operações.
    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override // Boa prática para depuração
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", status=" + status +
                '}';
    }
}