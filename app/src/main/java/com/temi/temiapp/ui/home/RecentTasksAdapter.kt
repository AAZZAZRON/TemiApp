package com.temi.temiapp.ui.home

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
import com.temi.temiapp.utils.Task

class RecentTasksAdapter(
    private val context: Context?,
    private val pinnedTasks: java.util.ArrayList<Task>
) : RecyclerView.Adapter<RecentTasksAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "RecentTasksAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.pinned_task_item, parent, false)
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.height = parent.height / 4
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return pinnedTasks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon = itemView.findViewById<ImageView>(R.id.icon)
        private val name = itemView.findViewById<TextView>(R.id.taskName)
        private val rerunTaskButton = itemView.findViewById<Button>(R.id.runTask)


        fun bind(position: Int) {
            Log.d(TAG, "runTask: ${position}")
            val task = pinnedTasks[position]
            icon.setImageResource(task.icon)
            name.text = task.name
            rerunTaskButton.text = "Rerun Task"
            rerunTaskButton.setOnClickListener { view ->
                Snackbar.make(view, "Rerunning \"${task.name}\"...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }
    }
}

