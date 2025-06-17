package com.example.projetoplanner.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id; // Tornar public para Room ou usar getters/setters

    private String nome;
    private String passwordHash; // Campo para armazenar o hash da senha
    private String salt;         // Campo para armazenar o salt da senha

    // Opcional: Campo para a URI da foto, conforme o requisito do trabalho
    private String photoUri;


    public User() {}

    // Construtor completo para cadastro
    public User(String nome, String passwordHash, String salt, String photoUri) {
        this.nome = nome;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.photoUri = photoUri;
    }

    // --- Getters e Setters ---

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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    @Override
    public String toString() {
        return id + ": " + getNome(); // Não inclua hash/salt no toString por segurança
    }
}