package com.example.firstapp.task

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapp.R
import com.example.firstapp.task.db.TasksEntity
import kotlinx.android.synthetic.main.item_task.view.*


class TaskAdapter internal constructor(context: Context) : RecyclerView.Adapter<TaskAdapter.TasksViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var listTasks = emptyList<TasksEntity>()
    lateinit var onClick: (TasksEntity) -> Unit
    lateinit var onLongClick: (TasksEntity) ->Unit


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TasksViewHolder(inflater.inflate(R.layout.item_task, parent, false))

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val current = listTasks[position]
        holder.bindResponse(current, onClick, onLongClick)
    }

    internal fun setItem(tasksEntityList: List<TasksEntity>?) {
        if (tasksEntityList != null) {
            this.listTasks = tasksEntityList
        }
        notifyDataSetChanged()
    }

    override fun getItemCount() = listTasks.size

    inner class TasksViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v

        fun bindResponse(task: TasksEntity, onClick: (TasksEntity) -> Unit, onLongClick: (TasksEntity) -> Unit) = with(itemView){
            view.item_name.text = task.taskName
            view.item_description.text = task.description
            view.item_status.text = task.status
            if (task.status == "In Progress"){
                view.item_status.setTextColor(context.getColor(R.color.inProgress))
            }
            if (task.status == "Complete" || task.firm == "true"){
                view.item_status.text = "Complete"
                view.item_status.setTextColor(context.getColor(R.color.complete))
            }
            val drawable = view.detail_layout.background as GradientDrawable
            drawable.setStroke(3, task.colorEventInt!!)

            setOnClickListener{ onClick(task) }
            setOnLongClickListener {
                onLongClick(task)
                true
            }
        }

    }
}