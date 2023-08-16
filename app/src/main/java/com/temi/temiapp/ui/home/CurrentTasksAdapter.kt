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
import com.temi.temiapp.utils.BackgroundTasks
import com.temi.temiapp.utils.CurrentTask
import com.temi.temiapp.utils.RunningTasksListener

class CurrentTasksAdapter(
    private val context: Context?,
) : RecyclerView.Adapter<CurrentTasksAdapter.ViewHolder>(), RunningTasksListener {
    private lateinit var recentAdapter: RecentTasksAdapter
    // make a copy of the running tasks
    private val myRunningTasks: ArrayList<CurrentTask> = ArrayList(BackgroundTasks.runningTasks)

    companion object {
        private const val TAG = "CurrentTasksAdapter"
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.current_task_item, parent, false)
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.height = parent.height / 3
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return myRunningTasks.size
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
            val currentTask: CurrentTask = myRunningTasks[position]
            icon.setImageResource(currentTask.task.icon)
            name.text = currentTask.task.name
            progressBar.progress = currentTask.progress
        }
    }

    fun onCreateListener() {
        BackgroundTasks.addListener(this)
        Log.d(TAG, "onCreateListener: ")
    }
    fun onDestroyListener() {
        BackgroundTasks.removeListener(this)
        Log.d(TAG, "onDestroyListener: ")
    }

    override fun onRunningTasksUpdatedAdd(currentTask: CurrentTask) {
        myRunningTasks.add(0, currentTask.copy())
        notifyItemInserted(0)

        Log.i("Add", BackgroundTasks.runningTasks.toString())
        Log.i("Add", myRunningTasks.toString())
    }

    override fun onRunningTasksUpdatedRemove(currentTask: CurrentTask) {
        val index = myRunningTasks.indexOf(currentTask)
        myRunningTasks.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun onRunningTasksUpdate(runningTasks: List<CurrentTask>) {
        Log.i("Add", BackgroundTasks.runningTasks.toString())
        Log.i("Add", myRunningTasks.toString())

        for (i in runningTasks.indices) {
            if (myRunningTasks[i] != runningTasks[i]) {
                myRunningTasks[i].progress = runningTasks[i].progress
                notifyItemChanged(i)
            }
        }
    }
}

