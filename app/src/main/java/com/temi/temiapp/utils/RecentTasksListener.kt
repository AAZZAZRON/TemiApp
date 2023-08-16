package com.temi.temiapp.utils

interface RecentTasksListener {
    fun onRecentTasksUpdatedAdd(completedTask: CompletedTask)
}
