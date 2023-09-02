package com.temi.temiapp.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.temi.temiapp.R
import com.temi.temiapp.utils.BackgroundTasks
import com.temi.temiapp.utils.CompletedTask
import com.temi.temiapp.utils.RecentTasksListener
import com.temi.temiapp.utils.Task
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Adapter for the task history recycler view
 */
class TaskHistoryAdapter (
    private val context: Context?,
    private val tasks: ArrayList<CompletedTask>
) : RecyclerView.Adapter<TaskHistoryAdapter.ViewHolder>(), RecentTasksListener {

    companion object {
        private const val TAG = "TaskHistoryAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.task_history_item, parent, false)
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.height = parent.height / 7 // TODO: make the height dynamic
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon = itemView.findViewById<ImageView>(R.id.icon)
        private val name = itemView.findViewById<TextView>(R.id.taskName)
        private val timestampText = itemView.findViewById<TextView>(R.id.timestamp)

        @SuppressLint("SetTextI18n", "SimpleDateFormat", "WeekBasedYear")
        fun bind(position: Int) {
            Log.d(TAG, "runTask: ${position}")
            val (task, option, timestamp) = tasks[position]
            icon.setImageResource(task.icon)
            name.text = task.name + " - " + option
            timestampText.text = "Ran on ${SimpleDateFormat("MMM dd, YYYY 'at' HH:mm:ss").format(Date(timestamp))}"
        }
    }


    fun onCreateListener() {
        BackgroundTasks.addRecentListener(this)
    }
    fun onDestroyListener() {
        BackgroundTasks.removeRecentListener(this)
    }
    override fun onRecentTasksUpdatedAdd(completedTask: CompletedTask) {
        tasks.add(0, completedTask)
        notifyItemInserted(0)
    }

}

