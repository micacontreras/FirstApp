package com.example.firstapp.task

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    fun getTask(taskName: String): LiveData<Tasks>? = repository.getTask(taskName)
    /*{
        var response: Tasks? = null
        /*viewModelScope.launch(Dispatchers.IO) {
            response = repository.getTask(taskName)
            Log.d("Room", response.toString())
        }*/
        return response
    }*/
}