package com.example.firstapp.task

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.firstapp.task.db.TaskDataBase
import com.example.firstapp.task.db.TasksEntity
import com.example.firstapp.task.db.TasksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TasksRepository

    var allTasksEntity: LiveData<List<TasksEntity>>? = null

    init {
        val tasksDao = TaskDataBase.getDatabase(application).taskDao()
        repository = TasksRepository(tasksDao)
    }

    fun getAllTasks() {
        allTasksEntity = repository.allTasksEntity
    }

    fun delete(taskName: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(taskName)
    }

    fun insert(task: TasksEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(task)
    }
}