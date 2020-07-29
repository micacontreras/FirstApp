package com.example.firstapp.task.db

import android.database.Cursor
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.example.firstapp.provider.TaskProvider


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

    fun selectAll(): Cursor? {
        return tasksDao.selectAll()
    }

    fun selectByName(name: String): Cursor? {
        return tasksDao.selectByName(name)
    }

    fun insertProvider(task: TasksEntity): Long {
        return tasksDao.insertProvider(task)
    }
}