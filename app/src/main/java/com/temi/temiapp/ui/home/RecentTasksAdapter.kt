package com.temi.temiapp.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
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
import com.temi.temiapp.utils.CompletedTask
import com.temi.temiapp.utils.StoredCompletedTask
import com.temi.temiapp.utils.Task
import com.temi.temiapp.utils.saveStoredCompletedTask
import java.text.SimpleDateFormat
import java.util.Date

class RecentTasksAdapter(
    private val context: Context?,
    private val allRecentTasks: ArrayList<StoredCompletedTask>,
    private val recentTasks: ArrayList<CompletedTask>,
    private val editor: SharedPreferences.Editor
) : RecyclerView.Adapter<RecentTasksAdapter.ViewHolder>() {

    private lateinit var currentAdapter: CurrentTasksAdapter

    companion object {
        private const val TAG = "RecentTasksAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.task_item_completed, parent, false)
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.height = parent.height / 4
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return recentTasks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    fun setCurrentAdapter(currentAdapter: CurrentTasksAdapter) {
        this.currentAdapter = currentAdapter
    }

    fun addTask(task: Task) {
        val timestamp: Long = System.currentTimeMillis()
        recentTasks.add(0, CompletedTask(task, timestamp))
        notifyItemInserted(0)
        if (recentTasks.size > 3) {
            recentTasks.removeAt(3)
            notifyItemRemoved(3)
        }
        allRecentTasks.add(0, StoredCompletedTask(task.id, timestamp))
        saveStoredCompletedTask(editor, "allRecentTasks", allRecentTasks)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon = itemView.findViewById<ImageView>(R.id.icon)
        private val name = itemView.findViewById<TextView>(R.id.taskName)
        private val timestampText = itemView.findViewById<TextView>(R.id.timestamp)
        private val rerunTaskButton = itemView.findViewById<Button>(R.id.runTask)


        @SuppressLint("SetTextI18n", "SimpleDateFormat", "WeekBasedYear")
        fun bind(position: Int) {
            Log.d(TAG, "runTask: ${position}")
            val (task, timestamp) = recentTasks[position]
            icon.setImageResource(task.icon)
            name.text = task.name
            timestampText.text = "Ran on ${SimpleDateFormat("MMM dd, YYYY 'at' HH:mm:ss").format(Date(timestamp))}"
            rerunTaskButton.setOnClickListener { view ->
                Snackbar.make(view, "Rerunning \"${task.name}\"...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                currentAdapter.addTask(task)
            }
        }
    }
}

