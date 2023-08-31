package com.temi.temiapp.ui.home

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.temi.temiapp.databinding.FragmentHomeBinding
import com.temi.temiapp.utils.ALL_TASKS
import com.temi.temiapp.utils.CompletedTask
import com.temi.temiapp.utils.ManageStorage
import com.temi.temiapp.utils.RESET
import com.temi.temiapp.utils.StoredTask
import com.temi.temiapp.utils.Task
import com.temi.temiapp.utils.alIntToJson
import com.temi.temiapp.utils.jsonToAlStoredTask
import com.temi.temiapp.utils.jsonToAlTask


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var currentAdapter: CurrentTasksAdapter
    private lateinit var recentAdapter: RecentTasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val root: View = binding.root


        val settings: SharedPreferences = requireActivity().getSharedPreferences("TemiApp", 0)
        val editor: SharedPreferences.Editor = settings.edit()
        ManageStorage.init(settings, editor)


        val loaded = settings.getBoolean("loaded", false)
        var pinnedTasks = ArrayList<Task>()
        var currentTasks = ArrayList<Task>()
        var allRecentTasks = ArrayList<StoredTask>()


        if (!loaded || RESET) {
            pinnedTasks = ALL_TASKS.filter { it.isPinned } as ArrayList
            currentTasks = ArrayList<Task>()
            allRecentTasks = ArrayList<StoredTask>()
            editor.putBoolean("loaded", true)
            editor.putString("pinnedTasks", alIntToJson(ArrayList(pinnedTasks.map { it.id })))
            editor.apply()
        } else {
            pinnedTasks = jsonToAlTask(settings.getString("pinnedTasks", "[]")!!)
            currentTasks = jsonToAlTask(settings.getString("currentTasks", "[]")!!)
            allRecentTasks = jsonToAlStoredTask(settings.getString("allRecentTasks", "[]")!!)
        }

        val recentTasks = ArrayList<CompletedTask>()
        for (i in 0..2) {
            if (i >= allRecentTasks.size) {
                break
            }
            val storedTask = allRecentTasks[i]
            val task = ALL_TASKS.find { it.id == storedTask.task }!!
            recentTasks.add(CompletedTask(task, storedTask.timestamp))
        }




        // current tasks
        val currentView: RecyclerView = binding.currentTasks
        currentAdapter = CurrentTasksAdapter(this.context)
        currentView.adapter = currentAdapter
        currentView.setHasFixedSize(true)
        currentView.layoutManager = LinearLayoutManager(this.context)
        currentAdapter.onCreateListener()


        // pinned tasks
        val pinnedView: RecyclerView = binding.pinnedTasks
        val pinnedAdapter = PinnedTasksAdapter(this.context, pinnedTasks, ::showPopup)
        pinnedView.adapter = pinnedAdapter
        pinnedView.setHasFixedSize(true)
        pinnedView.layoutManager = LinearLayoutManager(this.context)


        // recent tasks
        val recentView: RecyclerView = binding.recentTasks
        recentAdapter = RecentTasksAdapter(this.context, recentTasks, ::showPopup)
        recentView.adapter = recentAdapter
        recentView.setHasFixedSize(true)
        recentView.layoutManager = LinearLayoutManager(this.context)
        recentAdapter.onCreateListener()


        // allow show current tasks
        pinnedAdapter.setCurrentAdapter(currentAdapter)
        recentAdapter.setCurrentAdapter(currentAdapter)


        // allow show recent tasks
        currentAdapter.setRecentAdapter(recentAdapter)

        return root
    }

    @SuppressLint("InflateParams")
    fun showPopup(task: Task) {
        Log.i("HomeFragment", "showPopup $task")
        val inflater = LayoutInflater.from(requireContext())
        val popupView: View = inflater.inflate(com.temi.temiapp.R.layout.task_popup, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Set up the close button in the popup
        val closePopupButton: Button = popupView.findViewById(com.temi.temiapp.R.id.closePopupButton)
        closePopupButton.setOnClickListener {
            popupWindow.dismiss()
        }

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        currentAdapter.onDestroyListener()
        recentAdapter.onDestroyListener()
        _binding = null
    }
}