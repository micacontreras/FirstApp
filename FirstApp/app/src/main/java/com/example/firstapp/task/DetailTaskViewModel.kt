package com.example.firstapp.task

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.firstapp.task.db.TaskDataBase
import com.example.firstapp.task.db.Tasks
import com.example.firstapp.task.db.TasksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailTaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TasksRepository

    init {
        val tasksDao = TaskDataBase.getDatabase(application).taskDao()
        repository = TasksRepository(tasksDao)
    }

    fun insert(task: Tasks) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(task)
    }
    fun delete(taskName: String)= viewModelScope.launch(Dispatchers.IO) {
        repository.delete(taskName)
    }
}