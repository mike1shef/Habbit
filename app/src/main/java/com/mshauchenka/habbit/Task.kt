package com.mshauchenka.habbit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task (
    @PrimaryKey(autoGenerate = true)
    val taskID : Long = 0L,
    @ColumnInfo(name = "task_name")
    val title : String,
    @ColumnInfo(name = "task_completed")
    var completed : Boolean = false,
    @ColumnInfo(name = "task_completed_date")
    var dateCompleted : String? = null,
    @ColumnInfo(name = "task_current")
    var currentTask : Boolean = false
)

