package com.example.firstapp.task.db

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

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

    @Query("SELECT * FROM TasksEntity")
    fun selectAll(): Cursor?

    @Query("SELECT * FROM TasksEntity WHERE taskName= :name")
    fun selectByName(name: String): Cursor?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProvider(task: TasksEntity): Long
}
