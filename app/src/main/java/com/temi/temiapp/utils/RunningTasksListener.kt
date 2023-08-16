package com.temi.temiapp.utils

interface RunningTasksListener {
    fun onRunningTasksUpdate(currentTask: CurrentTask)
    fun onRunningTasksUpdatedAdd(index: Int)
    fun onRunningTasksUpdatedRemove(index: Int)
}
