package com.temi.temiapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.temi.temiapp.databinding.FragmentHomeBinding
import com.temi.temiapp.utils.ALL_TASKS
import com.temi.temiapp.utils.Task

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
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // recent deployments
        val recent_deployments: RecyclerView = binding.recentDeployments
        val recent_deployments_adapter = RecentDeploymentsAdapter(this.context)
        recent_deployments.adapter = recent_deployments_adapter
        recent_deployments.setHasFixedSize(true)
        recent_deployments.layoutManager = LinearLayoutManager(this.context)

        val items = ArrayList<String>()
        val fab: View = binding.fab2
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Deploying...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            items.add("Hello" + items.size)
            recent_deployments_adapter.setItems(items)
            recent_deployments_adapter.notifyItemInserted(items.size - 1)
        }


        // pinned tasks
        val pinnedView: RecyclerView = binding.pinnedTasks
        val pinnedTasks = ALL_TASKS.filter { it.isPinned } as ArrayList

        val pinnedAdapter = PinnedTasksAdapter(this.context, pinnedTasks)
        pinnedView.adapter = pinnedAdapter
        pinnedView.setHasFixedSize(true)
        pinnedView.layoutManager = LinearLayoutManager(this.context)


        // recent tasks
        val recentView: RecyclerView = binding.recentTasks
        val recentTasks = ALL_TASKS.filter {it.id == 1} as ArrayList<Task>

        val recentAdapter = RecentTasksAdapter(this.context, recentTasks)
        recentView.adapter = recentAdapter
        recentView.setHasFixedSize(true)
        recentView.layoutManager = LinearLayoutManager(this.context)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}