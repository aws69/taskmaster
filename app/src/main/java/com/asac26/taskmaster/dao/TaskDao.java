//package com.asac26.taskmaster.dao;
//
//import androidx.room.Dao;
//import androidx.room.Insert;
//import androidx.room.Query;
//
//import com.asac26.taskmaster.models.Task;
//
//import java.util.List;
//
//@Dao
//public interface TaskDao {
//    @Insert
//    void insertATask(Task task);
//
//    @Query("select * from Task")
//    List<Task> findAll();
//
//    @Query("select * from Task ORDER BY title ASC")
//    List<Task> findAllSortedByTitle();
//
//    @Query("select * from Task where id = :id")
//    Task findByAnId(long id);
//
//}