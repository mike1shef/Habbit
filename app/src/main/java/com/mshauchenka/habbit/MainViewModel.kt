package com.mshauchenka.habbit

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel (private val dao: TaskDao) : ViewModel() {
    private val _appState : MutableStateFlow<AppState> = MutableStateFlow(AppState.Start)
    val appState : StateFlow<AppState> get() = _appState


    val tasks = dao.getAll()
    val currentTask : MutableLiveData<Task> by lazy {
        MutableLiveData<Task>()
    }

    fun selectRandomTask () {
        val uncompletedTasks = tasks.value?.filter { !it.completed && it.title !== currentTask.value?.title }
        currentTask.value = uncompletedTasks!!.random()
    }

    fun addTask (taskTitle : String){
        viewModelScope.launch {
            val task = Task(title = taskTitle)
            dao.insert(task)
        }
    }
    fun completeTask (task: Task){
        viewModelScope.launch {
            task.completed = true
            dao.update(task)
        }
    }

    fun removeTask(task: Task) {
        viewModelScope.launch {
            dao.remove(task)
        }

    }

    fun openCardInBrowser() : Intent{
        val website = Uri.parse(currentTask.value!!.title)
        return Intent(Intent.ACTION_VIEW, website)
    }

    fun completeCurrentTask() {
        completeTask(currentTask.value!!)
    }

    fun uncompleteTask(task: Task) {
        viewModelScope.launch {
            task.completed = false
            dao.update(task)
        }
    }
}

sealed class AppState {
    object Start : AppState()
    object TaskSelected: AppState()
    object TaskFinished: AppState()
}