package com.temi.temiapp.utils

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * This object is used to manage the background tasks of the app.
 * It adds a task to the running tasks list, and executes the task, updating the progress.
 *
 * @property runningTasksListeners the list of listeners for running (current) tasks
 * @property recentTasksListeners the list of listeners for recent tasks
 * @property runningTasks the list of running (current) tasks
 */
object BackgroundTasks {
    private val runningTasksListeners: MutableList<RunningTasksListener> = mutableListOf()
    private val recentTasksListeners: MutableList<RecentTasksListener> = mutableListOf()
    val runningTasks: ArrayList<CurrentTask> = ArrayList()


    /**
     * Adds a task to the running tasks list, and executes the task, updating the progress.
     *
     * @param task the task to add
     * @param option the option chosen for the task
     */
    fun addTask(task: Task, option: String) {
        val currentTask = CurrentTask(task, option, 0)
        runningTasks.add(0, currentTask)
        notifyRunningListenersAdd(0)
        executeTask(currentTask)
    }

    /**
     * Removes a task from the running tasks list and notifies listeners.
     * Also adds the task to the recent tasks list.
     *
     * @param currentTask the task to remove
     */
    private fun removeTask(currentTask: CurrentTask) {
        val index: Int = runningTasks.indexOf(currentTask)
        runningTasks.remove(currentTask)
        notifyRunningListenersRemove(index)

        ManageStorage.addRecentTask(currentTask.task, currentTask.option)
        notifyRecentListenersAdd(CompletedTask(currentTask.task, currentTask.option, System.currentTimeMillis()))
    }

    /**
     * Executes a task on a separate thread, updating the progress.
     *
     * @param currentTask the task to execute
     */
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


    /**
     * Add or remove listeners for running (current) tasks.
     * @see RunningTasksListener
     */

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



    /**
     * Add or remove listeners for recent tasks.
     * @see RecentTasksListener
     */
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
