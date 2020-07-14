package com.example.firstapp.task.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TasksDao {
    @Query("SELECT * FROM Tasks")
    fun getAll(): LiveData<List<Tasks>>

    @Insert
    suspend fun insert(vararg task: Tasks)

    @Query("DELETE FROM tasks WHERE taskName = :taskName")
    suspend fun delete(taskName: String)

    @Query("SELECT * FROM Tasks WHERE taskName > :taskName")
    fun getTask(taskName: String): Tasks
}
