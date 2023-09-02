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
import com.temi.temiapp.utils.BackgroundTasks
import com.temi.temiapp.utils.CompletedTask
import com.temi.temiapp.utils.ManageStorage
import com.temi.temiapp.utils.RecentTasksListener
import com.temi.temiapp.utils.StoredTask
import com.temi.temiapp.utils.Task
import java.text.SimpleDateFormat
import java.util.Date

class RecentTasksAdapter(
    private val context: Context?,
    private val recentTasks: ArrayList<CompletedTask>,
    private val showPopupListener: (Task) -> Unit
) : RecyclerView.Adapter<RecentTasksAdapter.ViewHolder>(), RecentTasksListener {

    companion object {
        private const val TAG = "RecentTasksAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.completed_task_item, parent, false)
//        val layoutParams: ViewGroup.LayoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
//        layoutParams.height = parent.height / 4
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return recentTasks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon = itemView.findViewById<ImageView>(R.id.icon)
        private val name = itemView.findViewById<TextView>(R.id.taskName)
        private val timestampText = itemView.findViewById<TextView>(R.id.timestamp)
        private val rerunTaskButton = itemView.findViewById<Button>(R.id.runTask)


        @SuppressLint("SetTextI18n", "SimpleDateFormat", "WeekBasedYear")
        fun bind(position: Int) {
            Log.d(TAG, "runTask: ${position}")
            val (task, option, timestamp) = recentTasks[position]
            Log.d(TAG, "bindRecentTask: ${task.name} ${option} ${timestamp}")
            icon.setImageResource(task.icon)
            name.text = task.name
            timestampText.text = " - ${option}\nRan on ${SimpleDateFormat("MMM dd, YYYY 'at' HH:mm:ss").format(Date(timestamp))}"

            rerunTaskButton.setOnClickListener {
                showPopupListener.invoke(task)
            }
        }
    }


    /** add/remove listeners */
    fun onCreateListener() {
        BackgroundTasks.addRecentListener(this)
        Log.d(TAG, "onCreateRecentListener: ")
    }
    fun onDestroyListener() {
        BackgroundTasks.removeRecentListener(this)
        Log.d(TAG, "onDestroyRecentListener: ")
    }

    /** implement [RecentTasksListener] methods */
    override fun onRecentTasksUpdatedAdd(completedTask: CompletedTask) {
        recentTasks.add(0, completedTask)
        notifyItemInserted(0)
        if (recentTasks.size > 3) {
            recentTasks.removeAt(3)
            notifyItemRemoved(3)
        }
    }
}

