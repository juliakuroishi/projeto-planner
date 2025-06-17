package com.example.projetoplanner.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projetoplanner.entities.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    long insertTask(Task task);

    @Update
    int updateTask(Task task);

    @Delete
    int deleteTask(Task task);

    @Query("SELECT * FROM Task ORDER BY id DESC")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM Task WHERE status = :status ORDER BY id DESC")
    LiveData<List<Task>> getTasksByStatus(Boolean status);

    @Query("SELECT * FROM Task WHERE id = :taskId LIMIT 1")
    LiveData<Task> getTaskById(int taskId);
}



