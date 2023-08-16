package com.temi.temiapp.utils

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object BackgroundTasks {
    private val listeners: MutableList<RunningTasksListener> = mutableListOf()
    val runningTasks: ArrayList<CurrentTask> = ArrayList()


    fun addTask(task: Task) {
        val currentTask = CurrentTask(task, 0)
        runningTasks.add(0, currentTask)
        notifyListenersAdd(currentTask)
        executeTask(currentTask)
    }

    private fun removeTask(currentTask: CurrentTask) {
        runningTasks.remove(currentTask)
        notifyListenersRemove(currentTask)
    }

    private fun executeTask(currentTask: CurrentTask) {
        fun updateProgress(progress: Int) {
            currentTask.progress = progress
            notifyListeners()
            Log.i("BackgroundTasks", "Task ${currentTask.task.id} progress: $progress")
        }

        CoroutineScope(Dispatchers.IO).launch {
            currentTask.task.runTask(::updateProgress)
            Log.i("BackgroundTasks", "Task ${currentTask.task.id} completed")
            delay(1000)
            removeTask(currentTask)
        }
    }

    fun addListener(listener: RunningTasksListener) {
        listeners.add(listener)
        // Notify the listener immediately with the current list
        listener.onRunningTasksUpdate(runningTasks)
    }

    fun removeListener(listener: RunningTasksListener) {
        listeners.remove(listener)
    }

    private fun notifyListenersAdd(currentTask: CurrentTask) {
        for (listener in listeners) {
            listener.onRunningTasksUpdatedAdd(currentTask)
        }
    }

    private fun notifyListeners() {
        for (listener in listeners) {
            listener.onRunningTasksUpdate(runningTasks)
        }
    }

    private fun notifyListenersRemove(currentTask: CurrentTask) {
        for (listener in listeners) {
            listener.onRunningTasksUpdatedRemove(currentTask)
        }
    }
}
