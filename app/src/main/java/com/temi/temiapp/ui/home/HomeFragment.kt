package com.temi.temiapp.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.temi.temiapp.databinding.FragmentHomeBinding
import com.temi.temiapp.utils.ALL_TASKS
import com.temi.temiapp.utils.RESET
import com.temi.temiapp.utils.Task
import com.temi.temiapp.utils.convertArrayListToJson
import com.temi.temiapp.utils.convertJsonToArrayList

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
//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
//
//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

//        // recent deployments
//        val recent_deployments: RecyclerView = binding.recentDeployments
//        val recent_deployments_adapter = RecentDeploymentsAdapter(this.context)
//        recent_deployments.adapter = recent_deployments_adapter
//        recent_deployments.setHasFixedSize(true)
//        recent_deployments.layoutManager = LinearLayoutManager(this.context)
//
//        val items = ArrayList<String>()
//        val fab: View = binding.fab2
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Deploying...", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//            items.add("Hello" + items.size)
//            recent_deployments_adapter.setItems(items)
//            recent_deployments_adapter.notifyItemInserted(items.size - 1)
//        }

        val settings: SharedPreferences = requireActivity().getSharedPreferences("TemiApp", 0)
        val editor: SharedPreferences.Editor = settings.edit()
        val loaded = settings.getBoolean("loaded", false)
        var pinnedTasks = ArrayList<Task>()
        var currentTasks = ArrayList<Task>()
        var recentTasks = ArrayList<Task>()
        if (!loaded || RESET) {
            pinnedTasks = ALL_TASKS.filter { it.isPinned } as ArrayList
            currentTasks = ArrayList<Task>()
            recentTasks = ArrayList<Task>()
            editor.putBoolean("loaded", true)
            editor.putString("pinnedTasks", convertArrayListToJson(ArrayList(pinnedTasks.map { it.id })))
            editor.apply()
        } else {
            val pinnedTasksId = convertJsonToArrayList(settings.getString("pinnedTasks", "[]")!!)
            val currentTasksId = convertJsonToArrayList(settings.getString("currentTasks", "[]")!!)
            val recentTasksId = convertJsonToArrayList(settings.getString("recentTasks", "[]")!!)
            Log.d("all tasks", ALL_TASKS[0].id.toString() + pinnedTasksId.contains(1))
            pinnedTasks = ALL_TASKS.filter { pinnedTasksId.contains(it.id) } as ArrayList
            currentTasks = ALL_TASKS.filter { currentTasksId.contains(it.id) } as ArrayList
            recentTasks = ALL_TASKS.filter { recentTasksId.contains(it.id) } as ArrayList
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
        val recentAdapter = RecentTasksAdapter(this.context, recentTasks, editor)
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