package com.example.firstapp.task

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapp.R
import com.example.firstapp.task.db.Tasks
import kotlinx.android.synthetic.main.item_task.view.*

class TaskAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<TaskAdapter.TasksViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var list_tasks = emptyList<Tasks>()

    inner class TasksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.item_name
        val taskDescription: TextView = itemView.item_description
        val taskStatus: TextView = itemView.item_status
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TasksViewHolder(inflater.inflate(R.layout.item_task, parent, false))

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val current = list_tasks[position]
        holder.taskName.text = current.taskName
        holder.taskDescription.text = current.description
        holder.taskStatus.text = "To do"
    }

    internal fun setItem(tasksList: List<Tasks>) {
        this.list_tasks = tasksList
        notifyDataSetChanged()
    }

    override fun getItemCount() = list_tasks.size
}