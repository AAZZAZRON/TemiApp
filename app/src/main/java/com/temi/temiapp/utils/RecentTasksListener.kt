package com.temi.temiapp.utils

/**
 * This interface is used to notify listeners of recent tasks.
 * It is implemented by the HomeFragment.
 * Listeners for RecentTasksAdapter
 */
interface RecentTasksListener {
    fun onRecentTasksUpdatedAdd(completedTask: CompletedTask)
}
