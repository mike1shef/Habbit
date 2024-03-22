package com.mshauchenka.habbit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel (private val dao: TaskDao) : ViewModel() {
    val tasks = dao.getAll()

    fun addTask (taskTitle : String){
        viewModelScope.launch {
            val task = Task(title = taskTitle)
            dao.insert(task)
        }
    }
    fun completeTask (task: Task){
        viewModelScope.launch {
            task.completed = !task.completed
            dao.update(task)
        }
    }

    fun removeTask(task: Task) {
        viewModelScope.launch {
            dao.remove(task)
        }

    }
}