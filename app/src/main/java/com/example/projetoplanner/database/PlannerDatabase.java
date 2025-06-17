package com.example.projetoplanner.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.projetoplanner.dao.TaskDao;
import com.example.projetoplanner.dao.UserDao;
import com.example.projetoplanner.entities.Task;
import com.example.projetoplanner.entities.User;

@Database(entities = {User.class, Task.class}, version = 1, exportSchema = false)
public abstract class PlannerDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract TaskDao taskDao();

    private static volatile PlannerDatabase INSTANCE;

    public static PlannerDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlannerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    PlannerDatabase.class, "planner_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}