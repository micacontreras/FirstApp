package com.example.firstapp.task.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TasksDao {
    @Query("SELECT * FROM TasksEntity")
    fun getAll(): LiveData<List<TasksEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg task: TasksEntity)

    @Query("DELETE FROM TasksEntity WHERE taskName = :taskName")
    suspend fun delete(taskName: String)

    @Query("SELECT * FROM TasksEntity WHERE taskName = :taskName")
    fun getTask(taskName: String): LiveData<TasksEntity>
}
