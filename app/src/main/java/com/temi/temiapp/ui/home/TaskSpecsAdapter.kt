package com.temi.temiapp.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.temi.temiapp.R
import com.temi.temiapp.utils.Task
import com.temi.temiapp.utils.TaskSpec

/**
 * Adapter for the task specs recycler view in the task options popup.
 */
class TaskSpecsAdapter(
    private val context: Context?,
    private var specs: ArrayList<TaskSpec>,
) : RecyclerView.Adapter<TaskSpecsAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "PinnedTasksAdapter"

        /** The index of the currently pressed button. */
        private var currentPressed = -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.task_option_item, parent, false)
//        val layoutParams: ViewGroup.LayoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return specs.size
    }

    /**
     * Returns the index of the currently pressed button.
     * @see HomeFragment.showPopup
     */
    fun getCurrentPressed(): Int {
        return currentPressed
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val radioButton: RadioButton = itemView.findViewById(R.id.taskSpecOption)

        fun bind(position: Int) {
            Log.d(TAG, "notify: ${position}")
            val spec = specs[position]
            radioButton.text = spec.option
            radioButton.isChecked = spec.checked

            /** Update the chosen option when the user clicks on an option */
            radioButton.setOnClickListener {
                Log.d(TAG, "clicked: ${position}")
                if (currentPressed != -1) {
                    specs[currentPressed].checked = false
                    notifyItemChanged(currentPressed)
                }

                specs[position].checked = !specs[position].checked
                notifyItemChanged(position)
                currentPressed = position
            }
        }
    }
}
