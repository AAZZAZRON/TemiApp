package com.temi.temiapp.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.temi.temiapp.R
import com.temi.temiapp.utils.CompletedTask
import com.temi.temiapp.utils.Task
import kotlinx.coroutines.*

class CurrentTasksAdapter(
    private val context: Context?,
) : RecyclerView.Adapter<CurrentTasksAdapter.ViewHolder>() {
    private lateinit var recentAdapter: RecentTasksAdapter
    private val currentTasks = ArrayList<Task>()

    companion object {
        private const val TAG = "CurrentTasksAdapter"
        private val coroutineDispatcher = Dispatchers.Default
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.current_task_item, parent, false)
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.height = parent.height / 3
        return ViewHolder(view)
    }

    fun addTask(task: Task) {
        currentTasks.add(0, task)
        notifyItemInserted(0)
    }

    fun runAddedTask(task: Task, progressBar: ProgressBar) {
        // coroutine
        CoroutineScope(coroutineDispatcher).launch {
            task.executeTask(this@CurrentTasksAdapter, progressBar)
        }
    }

    fun removeTask(task: Task) {
        val index = currentTasks.indexOf(task)
        currentTasks.removeAt(index)
        notifyItemRemoved(index)
        recentAdapter.addTask(task)
    }


    override fun getItemCount(): Int {
        return currentTasks.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    fun setRecentAdapter(recentAdapter: RecentTasksAdapter) {
        this.recentAdapter = recentAdapter
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon = itemView.findViewById<ImageView>(R.id.icon)
        private val name = itemView.findViewById<TextView>(R.id.taskName)
        private val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar)

        fun bind(position: Int) {
            Log.d(TAG, "runTask: ${position}")
            val task = currentTasks[position]
            icon.setImageResource(task.icon)
            name.text = task.name

            // get a reference
            runAddedTask(task, progressBar)
        }
    }
}

