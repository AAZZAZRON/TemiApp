package com.temi.temiapp.ui.home

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.temi.temiapp.databinding.FragmentHomeBinding
import com.temi.temiapp.utils.ALL_TASKS
import com.temi.temiapp.utils.BackgroundTasks
import com.temi.temiapp.utils.CompletedTask
import com.temi.temiapp.utils.ManageStorage
import com.temi.temiapp.utils.RESET
import com.temi.temiapp.utils.StoredTask
import com.temi.temiapp.utils.Task
import com.temi.temiapp.utils.alIntToJson
import com.temi.temiapp.utils.jsonToAlStoredTask
import com.temi.temiapp.utils.jsonToAlTask


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 * This is the home screen of the app.
 * It displays the current tasks, pinned tasks, and recent tasks.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    /** Keep references to the adapters for the recycler views to add/remove listeners */
    private lateinit var currentAdapter: CurrentTasksAdapter
    private lateinit var recentAdapter: RecentTasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val root: View = binding.root

        // shared preferences for persistent storage
        val settings: SharedPreferences = requireActivity().getSharedPreferences("TemiApp", 0)
        val editor: SharedPreferences.Editor = settings.edit()
        ManageStorage.init(settings, editor)


        val loaded = settings.getBoolean("loaded", false)
        val pinnedTasks: ArrayList<Task>
        val allRecentTasks: ArrayList<StoredTask>



        /** load in values from persistent storage */
        if (!loaded || RESET) {
            pinnedTasks = ALL_TASKS.filter { it.isPinned } as ArrayList
            allRecentTasks = ArrayList<StoredTask>()
            editor.putBoolean("loaded", true)
            editor.putString("pinnedTasks", alIntToJson(ArrayList(pinnedTasks.map { it.id })))
            editor.putString("allRecentTasks", "[]")
            editor.apply()
        } else {
            pinnedTasks = jsonToAlTask(settings.getString("pinnedTasks", "[]")!!)
            allRecentTasks = jsonToAlStoredTask(settings.getString("allRecentTasks", "[]")!!)
        }

        /** convert the stored tasks (how they are stored in SharedPreferences) to completed tasks */
        val recentTasks = ArrayList<CompletedTask>()
        for (i in 0..2) {
            if (i >= allRecentTasks.size) {
                break
            }
            val storedTask = allRecentTasks[i]
            val task = ALL_TASKS.find { it.id == storedTask.task }!!
            recentTasks.add(CompletedTask(task, storedTask.option, storedTask.timestamp))
        }



        /** Set up the recycler views and adapters */

        // current tasks
        val currentView: RecyclerView = binding.currentTasks
        currentAdapter = CurrentTasksAdapter(this.context)
        currentView.adapter = currentAdapter
        currentView.setHasFixedSize(true)
        currentView.layoutManager = LinearLayoutManager(this.context)
        currentAdapter.onCreateListener() /** @see BackgroundTasks.addRunningListener */


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
        recentAdapter.onCreateListener() /** @see BackgroundTasks.addRecentListener */


        return root
    }


    /**
     * Show a popup window to choose the options for a task
     * @param task the task to run
     */
    @SuppressLint("InflateParams")
    fun showPopup(task: Task) {
        Log.i("HomeFragment", "showPopup $task")

        /** Set up the popup window */
        val inflater = LayoutInflater.from(requireContext())
        val popupView: View = inflater.inflate(com.temi.temiapp.R.layout.task_popup, null)

        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels

        val popupWidth = (screenWidth * 0.5).toInt()
        val popupHeight = (screenHeight * 0.7).toInt()

        val popupWindow = PopupWindow(
            popupView,
            popupWidth,
            popupHeight,
            true
        )


        /** Customize based on task */
        val popupHeader: TextView = popupView.findViewById(com.temi.temiapp.R.id.popupHeader)
        popupHeader.text = task.name

        // set up the task spec info
        val taskSpecs = popupView.findViewById<RecyclerView>(com.temi.temiapp.R.id.taskSpecs)
        val taskSpecsAdapter = TaskSpecsAdapter(this.context, task.specs)
        taskSpecs.adapter = taskSpecsAdapter
        taskSpecs.setHasFixedSize(true)
        taskSpecs.layoutManager = LinearLayoutManager(this.context)



        /** When the run button is pressed, run the option selected if any */
        val runTaskButton: Button = popupView.findViewById(com.temi.temiapp.R.id.runTaskButton)
        runTaskButton.setOnClickListener {
            val chosenOptionInd: Int = taskSpecsAdapter.getCurrentPressed()

            if (chosenOptionInd == -1) { // error trap for no values chosen
                Snackbar.make(it, "Please choose an option", Snackbar.LENGTH_LONG).setAction("Action", null).show()
                return@setOnClickListener
            }

            /** assert that an option was chosen */

            val chosenOption = task.specs[chosenOptionInd].option
            Log.i("HomeFragment", "chosenOption: $chosenOption")
            task.specs[chosenOptionInd].checked = false
            popupWindow.dismiss() // close window

            /** run the task */
            BackgroundTasks.addTask(task, chosenOption)
        }

        /** Show the popup window */
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        currentAdapter.onDestroyListener()
        recentAdapter.onDestroyListener()
        _binding = null
    }
}