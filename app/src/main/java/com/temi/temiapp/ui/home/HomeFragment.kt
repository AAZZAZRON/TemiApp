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
import com.temi.temiapp.MainActivity
import com.temi.temiapp.R
import com.temi.temiapp.databinding.FragmentHomeBinding

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

        val recent_deployments: RecyclerView = binding.recentDeployments
        val adapter = RecentDeploymentsAdapter(this.context)
        recent_deployments.adapter = adapter
        recent_deployments.setHasFixedSize(true)
        recent_deployments.layoutManager = LinearLayoutManager(this.context)

        val items = ArrayList<String>()
        val fab: View = binding.fab2
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Deploying...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            items.add("Hello" + items.size)
            adapter.setItems(items)
            adapter.notifyItemInserted(items.size - 1)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}