package com.example.firstapp.task

import android.app.Application
import androidx.lifecycle.*
import com.example.firstapp.task.db.TaskDataBase
import com.example.firstapp.task.db.Tasks
import com.example.firstapp.task.db.TasksRepository

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TasksRepository

    val allTasks: LiveData<List<Tasks>>

    init {
        val tasksDao = TaskDataBase.getDatabase(application).taskDao()
        repository = TasksRepository(tasksDao)
        allTasks = repository.allTasks
    }
}