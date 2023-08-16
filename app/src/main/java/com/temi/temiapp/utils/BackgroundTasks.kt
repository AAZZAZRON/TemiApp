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
        notifyListenersAdd(0)
        executeTask(currentTask)
    }

    private fun removeTask(currentTask: CurrentTask) {
        val index: Int = runningTasks.indexOf(currentTask)
        runningTasks.remove(currentTask)
        notifyListenersRemove(index)
    }

    private fun executeTask(currentTask: CurrentTask) {
        fun updateProgress(progress: Int) {
            currentTask.progress = progress
            notifyListeners(currentTask)
            Log.i("BackgroundTasks", "Task ${currentTask.task.id} progress: $progress")
        }

        CoroutineScope(Dispatchers.IO).launch {
            currentTask.task.runTask(::updateProgress)
            Log.i("BackgroundTasks", "Task ${currentTask.task.id} completed")
            delay(1000)
            removeTask(currentTask) // remove task
        }
    }

    fun addListener(listener: RunningTasksListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: RunningTasksListener) {
        listeners.remove(listener)
    }

    private fun notifyListenersAdd(index: Int) {
        for (listener in listeners) {
            listener.onRunningTasksUpdatedAdd(index)
        }
    }

    private fun notifyListeners(currentTask: CurrentTask) {
        for (listener in listeners) {
            listener.onRunningTasksUpdate(currentTask)
        }
    }

    private fun notifyListenersRemove(index: Int) {
        for (listener in listeners) {
            listener.onRunningTasksUpdatedRemove(index)
        }
    }
}
