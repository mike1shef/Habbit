package com.mshauchenka.habbit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter () : RecyclerView.Adapter<TasksViewHolder>() {
    var data = listOf<Task>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {

        val holder = TasksViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false))


        holder.removeButton.setOnClickListener {
            val position = holder.adapterPosition
            notifyItemChanged(position)
        }

        holder.taskCheckbox.setOnClickListener {
            val position = holder.adapterPosition
            data[position].apply {
                this.completed = !this.completed
            }
        }

        return holder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val item = data[position]
        holder.taskText.text = item.title
        holder.taskCheckbox.isChecked = item.completed
    }


}

class TasksViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val taskText = view.findViewById<TextView>(R.id.item_task_text)
    val taskCheckbox = view.findViewById<CheckBox>(R.id.item_task_checkbox)
    val removeButton = view.findViewById<ImageButton>(R.id.item_task_remove_button)
}

class TaskDiffUtilCallback : DiffUtil.ItemCallback<Task>(){
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean
    = (oldItem.title == newItem.title && oldItem.completed == newItem.completed)

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        TODO("Not yet implemented")
    }

    override fun getChangePayload(oldItem: Task, newItem: Task): Any? {
        return super.getChangePayload(oldItem, newItem)
    }
}