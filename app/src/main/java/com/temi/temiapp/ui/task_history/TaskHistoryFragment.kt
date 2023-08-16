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
import com.temi.temiapp.ui.home.TaskHistoryAdapter
import com.temi.temiapp.utils.ALL_TASKS
import com.temi.temiapp.utils.CompletedTask
import com.temi.temiapp.utils.StoredTask
import com.temi.temiapp.utils.jsonToAlStoredTask

class TaskHistoryFragment : Fragment() {

    private var _binding: FragmentTaskHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var taskHistoryAdapter: TaskHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val settings: SharedPreferences = requireActivity().getSharedPreferences("TemiApp", 0)
        val allRecentTasks: ArrayList<StoredTask> = jsonToAlStoredTask(settings.getString("allRecentTasks", "[]")!!)
        val recentTasks: ArrayList<CompletedTask> = allRecentTasks.map { CompletedTask(ALL_TASKS.find { task -> task.id == it.task }!!, it.timestamp) } as ArrayList<CompletedTask>

        val taskHistoryView: RecyclerView = binding.taskHistory
        taskHistoryAdapter = TaskHistoryAdapter(this.context, recentTasks)
        taskHistoryView.adapter = taskHistoryAdapter
        taskHistoryView.setHasFixedSize(true)
        taskHistoryView.layoutManager = LinearLayoutManager(context)
        taskHistoryAdapter.onCreateListener()



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        taskHistoryAdapter.onDestroyListener()
        _binding = null
    }
}