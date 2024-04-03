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
//    private val _appState : MutableStateFlow<AppState> = MutableStateFlow(AppState.Start)
//    val appState : StateFlow<AppState> get() = _appState


    val tasks = dao.getAll()
    val currentTask : MutableLiveData<Task> by lazy {
        MutableLiveData<Task>()
    }

    fun getCurrentTask() {
        if (!tasks.value.isNullOrEmpty()){
            currentTask.value = tasks.value?.firstOrNull { it.currentTask }
            if (currentTask.value == null){
                currentTask.value = selectRandomTask()
                updateCurrentTask(currentTask.value!!)
            }
        }
    }

    fun selectRandomTask() : Task {
        val uncompletedTasks = tasks.value?.filter { !it.completed && !it.currentTask }
        return uncompletedTasks!!.random()
    }

    fun mixCurrentTask(){
        val newTask = selectRandomTask()
        updateCurrentTask(currentTask.value!!)
        currentTask.value = newTask
        updateCurrentTask(currentTask.value!!)
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
            if (task.currentTask){
                task.currentTask = false
                getCurrentTask()
                dao.update(task)
            }
        }
    }

    fun removeTask(task: Task) {
        viewModelScope.launch {
            if (!task.currentTask){
                dao.remove(task)
            } else {
                dao.remove(task)
                getCurrentTask()
            }
        }
    }

    fun openCardInBrowser() : Intent{
        val website = Uri.parse(currentTask.value!!.title)
        return Intent(Intent.ACTION_VIEW, website)
    }

    fun completeCurrentTask() {
        completeTask(currentTask.value!!)
    }

    fun unCompleteTask(task: Task) {
        viewModelScope.launch {
            task.completed = false
            dao.update(task)
        }
    }
    fun updateCurrentTask(task: Task) {
        viewModelScope.launch {
            if (task.currentTask){
                task.currentTask = false
            } else task.currentTask = true
            dao.update(task)
        }
    }
}

//sealed class AppState {
//    object Start : AppState()
//    object TaskSelected: AppState()
//    object TaskFinished: AppState()
//}