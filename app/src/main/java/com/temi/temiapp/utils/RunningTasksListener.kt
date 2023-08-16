package com.temi.temiapp.utils

interface RunningTasksListener {
    fun onRunningTasksUpdate(runningTasks: List<CurrentTask>)
    fun onRunningTasksUpdatedAdd(currentTask: CurrentTask)
    fun onRunningTasksUpdatedRemove(currentTask: CurrentTask)
}
