package com.example.projetoplanner.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projetoplanner.entities.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    long insertUser(User user);

    @Update // Adicione se precisar atualizar dados do usuário (ex: foto, senha)
    int updateUser(User user);

    // Método para buscar um usuário pelo nome (útil para login)
    @Query("SELECT * FROM User WHERE nome = :userName LIMIT 1")
    User getUserByNome(String userName);

    // Método para buscar um usuário pelo ID (útil se precisar carregar o usuário logado)
    @Query("SELECT * FROM User WHERE id = :userId LIMIT 1")
    User getUserById(int userId);

    // Método para deletar um usuário (se necessário)
     @Delete
    int deleteUser(User user);

    // Método para obter todos os usuários (raramente usado em apps de usuário único, mas pode ser útil para debug)
   @Query("SELECT * FROM User")
 List<User> getAllUsers();
}