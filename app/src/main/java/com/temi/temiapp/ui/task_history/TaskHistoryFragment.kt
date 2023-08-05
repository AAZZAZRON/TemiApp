package com.temi.temiapp.ui.task_history

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.temi.temiapp.databinding.FragmentTaskHistoryBinding
import com.temi.temiapp.ui.home.CurrentTasksAdapter
import com.temi.temiapp.ui.home.TaskHistoryAdapter
import com.temi.temiapp.utils.ALL_TASKS
import com.temi.temiapp.utils.CompletedTask
import com.temi.temiapp.utils.StoredCompletedTask
import com.temi.temiapp.utils.jsonToAlStoredCompletedTask

class TaskHistoryFragment : Fragment() {

    private var _binding: FragmentTaskHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val settings: SharedPreferences = requireActivity().getSharedPreferences("TemiApp", 0)
        val allRecentTasks: ArrayList<StoredCompletedTask> = jsonToAlStoredCompletedTask(settings.getString("allRecentTasks", "[]")!!)
        val recentTasks: ArrayList<CompletedTask> = allRecentTasks.map { CompletedTask(ALL_TASKS.find { task -> task.id == it.task }!!, it.timestamp) } as ArrayList<CompletedTask>

        val taskHistoryView: RecyclerView = binding.taskHistory
        val taskHistoryAdapter = TaskHistoryAdapter(this.context, recentTasks)
        taskHistoryView.adapter = taskHistoryAdapter
        taskHistoryView.setHasFixedSize(true)
        taskHistoryView.layoutManager = LinearLayoutManager(context)



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}