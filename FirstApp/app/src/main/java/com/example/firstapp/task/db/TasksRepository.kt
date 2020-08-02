package com.example.firstapp.task.db

import android.database.Cursor
import androidx.lifecycle.LiveData
import java.util.*


class TasksRepository(private val tasksDao: TasksDao) {

    val allTasksEntity: LiveData<List<TasksEntity>> = tasksDao.getAll()

    suspend fun insert(task: TasksEntity) {
        tasksDao.insert(task)
    }

    suspend fun update(id: Long, tit: String?, descript: String?, date: Date?, time: Date?, color: String?, colorInt: Int?){
        tasksDao.update(id, tit, descript, date, time, color, colorInt)
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