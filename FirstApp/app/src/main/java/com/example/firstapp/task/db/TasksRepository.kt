package com.example.firstapp.task.db

import android.database.Cursor
import androidx.lifecycle.LiveData
import java.util.*


class TasksRepository(private val tasksDao: TasksDao) {

    val allTasksEntity: LiveData<List<TasksEntity>> = tasksDao.getAll()

    suspend fun insert(task: TasksEntity) {
        tasksDao.insert(task)
    }

    suspend fun delete(taskName: String) {
        tasksDao.delete(taskName)
    }

    fun getTask(taskName: String): LiveData<TasksEntity> {
        return tasksDao.getTask(taskName)
    }
}