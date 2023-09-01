package com.temi.temiapp.utils

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object BackgroundTasks {
    private val runningTasksListeners: MutableList<RunningTasksListener> = mutableListOf()
    private val recentTasksListeners: MutableList<RecentTasksListener> = mutableListOf()
    val runningTasks: ArrayList<CurrentTask> = ArrayList()


    fun addTask(task: Task, option: String) {
        val currentTask = CurrentTask(task, option, 0)
        runningTasks.add(0, currentTask)
        notifyRunningListenersAdd(0)
        executeTask(currentTask)
    }

    private fun removeTask(currentTask: CurrentTask) {
        val index: Int = runningTasks.indexOf(currentTask)
        runningTasks.remove(currentTask)
        notifyRunningListenersRemove(index)

        ManageStorage.addRecentTask(currentTask.task, currentTask.option)
        notifyRecentListenersAdd(CompletedTask(currentTask.task, currentTask.option, System.currentTimeMillis()))
    }

    private fun executeTask(currentTask: CurrentTask) {
        fun updateProgress(progress: Int) {
            currentTask.progress = progress
            notifyRunningListenersUpdate(currentTask)
            Log.i("BackgroundTasks", "Task ${currentTask.task.id} progress: $progress")
        }

        CoroutineScope(Dispatchers.IO).launch {
            currentTask.task.runTask(::updateProgress)
            Log.i("BackgroundTasks", "Task ${currentTask.task.id} completed")
            delay(1000)
            removeTask(currentTask) // remove task
        }
    }

    fun addRunningListener(listener: RunningTasksListener) {
        runningTasksListeners.add(listener)
    }

    fun removeRunningListener(listener: RunningTasksListener) {
        runningTasksListeners.remove(listener)
    }

    private fun notifyRunningListenersAdd(index: Int) {
        for (listener in runningTasksListeners) {
            listener.onRunningTasksUpdatedAdd(index)
        }
    }

    private fun notifyRunningListenersUpdate(currentTask: CurrentTask) {
        for (listener in runningTasksListeners) {
            listener.onRunningTasksUpdate(currentTask)
        }
    }

    private fun notifyRunningListenersRemove(index: Int) {
        for (listener in runningTasksListeners) {
            listener.onRunningTasksUpdatedRemove(index)
        }
    }

    fun addRecentListener(listener: RecentTasksListener) {
        recentTasksListeners.add(listener)
    }

    fun removeRecentListener(listener: RecentTasksListener) {
        recentTasksListeners.remove(listener)
    }

    private fun notifyRecentListenersAdd(completedTask: CompletedTask) {
        for (listener in recentTasksListeners) {
            listener.onRecentTasksUpdatedAdd(completedTask)
        }
    }
}
