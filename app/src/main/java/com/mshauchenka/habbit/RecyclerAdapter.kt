package com.mshauchenka.habbit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter (private val vm : MainViewModel)
    : ListAdapter <Task, RecyclerAdapter.TasksViewHolder>(TaskDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val holder = TasksViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_task, parent, false))


            holder.removeButton.setOnClickListener {
                val item = getItem(holder.adapterPosition)
                if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                    vm.removeTask(item)
                }
            }

            holder.taskCheckbox.setOnClickListener {
                val item = getItem(holder.adapterPosition)
                if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                    if (item.completed){
                        vm.unCompleteTask(item)
                    } else vm.completeTask(item)
                }
            }

        return holder
    }


    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val item = getItem(position)
        holder.taskText.text = item.title
        holder.taskCheckbox.isChecked = item.completed

    }

    class TasksViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskText = view.findViewById<TextView>(R.id.item_task_text)
        val taskCheckbox = view.findViewById<CheckBox>(R.id.item_task_checkbox)
        val removeButton = view.findViewById<ImageButton>(R.id.item_task_remove_button)
    }
}
class TaskDiffUtilCallback : DiffUtil.ItemCallback<Task>(){
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean
    = (oldItem.taskID == newItem.taskID)

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean = (oldItem == newItem)

    override fun getChangePayload(oldItem: Task, newItem: Task): Any? {
        return super.getChangePayload(oldItem, newItem)
    }
}