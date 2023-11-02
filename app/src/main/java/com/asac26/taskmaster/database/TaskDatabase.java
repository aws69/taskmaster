package com.asac26.taskmaster.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.asac26.taskmaster.dao.TaskDao;
import com.asac26.taskmaster.models.Task;

@Database(entities = {Task.class}, version = 1)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}