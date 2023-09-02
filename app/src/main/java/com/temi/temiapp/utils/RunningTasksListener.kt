package com.temi.temiapp.utils

/**
 * This interface is used to notify listeners of running tasks.
 * It is implemented by the HomeFragment.
 * Listeners for CurrentTaskAdapter
 */
interface RunningTasksListener {
    fun onRunningTasksUpdate(currentTask: CurrentTask)
    fun onRunningTasksUpdatedAdd(index: Int)
    fun onRunningTasksUpdatedRemove(index: Int)
}
