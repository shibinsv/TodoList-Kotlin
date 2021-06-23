package com.example.todolist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.data.Task
import com.example.todolist.databinding.ItemTaskBinding

class TasksAdapter(private val listener: OnItemClickListener) : ListAdapter<Task,TasksAdapter.TodoViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentItem=getItem(position)
        holder.bind(currentItem)
    }

    inner class TodoViewHolder(private val binding : ItemTaskBinding):RecyclerView.ViewHolder(binding.root) {

        init{
            binding.apply {
                root.setOnClickListener{
                    val position = adapterPosition
                    if (position!= RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onItemClick(task)

                    }
                }

                checkBox.setOnClickListener {
                    val position = adapterPosition
                    if (position!=RecyclerView.NO_POSITION){
                        val task = getItem(position)
                        listener.onCheckBocClicked(task,checkBox.isChecked)
                    }
                }


            }

        }
        fun bind(task: Task){
            binding.apply {
                checkBox.isChecked = task.completed
                itemName.text = task.name
                itemName.paint.isStrikeThruText=task.completed
                priorityLabel.isVisible=task.important

            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(task: Task)
        fun onCheckBocClicked(task: Task,isChecked :Boolean)
    }


    class DiffCallback : DiffUtil.ItemCallback<Task>(){

        override fun areItemsTheSame(oldItem: Task, newItem: Task)= oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem

    }

}