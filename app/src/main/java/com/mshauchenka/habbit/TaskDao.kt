package com.mshauchenka.habbit

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task : Task)
    @Insert
    suspend fun insertAll(listOfTasks :List<Task>)

    @Update
    suspend fun update(task : Task)

    @Delete
    suspend fun remove(task: Task)

    @Query("SELECT * FROM task_table WHERE taskID = :taskId")
    fun get (taskId: Long) : LiveData<Task>

    @Query("SELECT * FROM task_table WHERE task_completed = 0")
    fun getUncompleted () : LiveData<List<Task>>

    @Query("SELECT * FROM task_table ORDER BY taskID DESC")
    fun getAll () : LiveData<List<Task>>
}