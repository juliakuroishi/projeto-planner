package com.example.projetoplanner.utils;

import android.util.Base64; // Para codificar/decodificar bytes para String

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtils {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 10000; // Número de iterações
    private static final int KEY_LENGTH = 256;   // Comprimento da chave (hash) em bits

    // Gera um salt aleatório (bytes) e o converte para String Base64
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16]; // 16 bytes = 128 bits de salt
        random.nextBytes(salt);
        return Base64.encodeToString(salt, Base64.DEFAULT);
    }

    // Gera o hash de uma senha com um salt dado
    public static String hashPassword(String password, String salt) {
        try {
            byte[] saltBytes = Base64.decode(salt, Base64.DEFAULT);
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return Base64.encodeToString(hash, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null; // Trate esse erro adequadamente no app
        }
    }

    // Verifica se uma senha em texto plano corresponde a um hash armazenado
    public static boolean verifyPassword(String plainPassword, String storedHash, String storedSalt) {
        String newHash = hashPassword(plainPassword, storedSalt);
        return newHash != null && newHash.equals(storedHash);
    }
}