package com.example.firstapp.task.db

import androidx.lifecycle.LiveData

class TasksRepository(private val tasksDao: TasksDao) {

    val allTasks: LiveData<List<Tasks>> = tasksDao.getAll()

    suspend fun insert(task: Tasks) {
        tasksDao.insert(task)
    }

    suspend fun delete(taskName: String){
        tasksDao.delete(taskName)
    }

    fun getTask(taskName: String): LiveData<Tasks>{
        return tasksDao.getTask(taskName)
    }

}