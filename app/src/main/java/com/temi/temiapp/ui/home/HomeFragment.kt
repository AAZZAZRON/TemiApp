package com.temi.temiapp.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.temi.temiapp.databinding.FragmentHomeBinding
import com.temi.temiapp.utils.ALL_TASKS
import com.temi.temiapp.utils.CompletedTask
import com.temi.temiapp.utils.RESET
import com.temi.temiapp.utils.StoredCompletedTask
import com.temi.temiapp.utils.Task
import com.temi.temiapp.utils.alIntToJson
import com.temi.temiapp.utils.jsonToAlStoredCompletedTask
import com.temi.temiapp.utils.jsonToAlTask

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val settings: SharedPreferences = requireActivity().getSharedPreferences("TemiApp", 0)
        val editor: SharedPreferences.Editor = settings.edit()
        val loaded = settings.getBoolean("loaded", false)
        var pinnedTasks = ArrayList<Task>()
        var currentTasks = ArrayList<Task>()
        var allRecentTasks = ArrayList<StoredCompletedTask>()
        if (!loaded || RESET) {
            pinnedTasks = ALL_TASKS.filter { it.isPinned } as ArrayList
            currentTasks = ArrayList<Task>()
            allRecentTasks = ArrayList<StoredCompletedTask>()
            editor.putBoolean("loaded", true)
            editor.putString("pinnedTasks", alIntToJson(ArrayList(pinnedTasks.map { it.id })))
            editor.apply()
        } else {
            pinnedTasks = jsonToAlTask(settings.getString("pinnedTasks", "[]")!!)
            currentTasks = jsonToAlTask(settings.getString("currentTasks", "[]")!!)
            allRecentTasks = jsonToAlStoredCompletedTask(settings.getString("allRecentTasks", "[]")!!)
        }

        val recentTasks = ArrayList<CompletedTask>()
        for (i in 0..2) {
            if (i >= allRecentTasks.size) {
                break
            }
            val storedCompletedTask = allRecentTasks[i]
            val task = ALL_TASKS.find { it.id == storedCompletedTask.task }!!
            recentTasks.add(CompletedTask(task, storedCompletedTask.timestamp))
        }


        // current tasks
        val currentView: RecyclerView = binding.currentTasks
        val currentAdapter = CurrentTasksAdapter(this.context)
        currentView.adapter = currentAdapter
        currentView.setHasFixedSize(true)
        currentView.layoutManager = LinearLayoutManager(this.context)


        // pinned tasks
        val pinnedView: RecyclerView = binding.pinnedTasks
        val pinnedAdapter = PinnedTasksAdapter(this.context, pinnedTasks)
        pinnedView.adapter = pinnedAdapter
        pinnedView.setHasFixedSize(true)
        pinnedView.layoutManager = LinearLayoutManager(this.context)


        // recent tasks
        val recentView: RecyclerView = binding.recentTasks
        val recentAdapter = RecentTasksAdapter(this.context, allRecentTasks, recentTasks, editor)
        recentView.adapter = recentAdapter
        recentView.setHasFixedSize(true)
        recentView.layoutManager = LinearLayoutManager(this.context)


        // allow show current tasks
        pinnedAdapter.setCurrentAdapter(currentAdapter)
        recentAdapter.setCurrentAdapter(currentAdapter)


        // allow show recent tasks
        currentAdapter.setRecentAdapter(recentAdapter)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}