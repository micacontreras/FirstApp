package com.example.firstapp.task

import android.app.Application
import androidx.lifecycle.*
import com.example.firstapp.task.db.TaskDataBase
import com.example.firstapp.task.db.Tasks
import com.example.firstapp.task.db.TasksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TasksRepository

    var allTasks: LiveData<List<Tasks>>? = null

    init {
        val tasksDao = TaskDataBase.getDatabase(application).taskDao()
        repository = TasksRepository(tasksDao)
    }

    fun getAllTasks(){
        allTasks = repository.allTasks
    }

    fun delete(taskName: String)= viewModelScope.launch(Dispatchers.IO) {
        repository.delete(taskName)
    }
}