package com.temi.temiapp.utils

import android.content.SharedPreferences

/**
 * This object is used to manage the persistent storage of the app.
 * It adds a task to the recent tasks list, and saves the list to the shared preferences.
 *
 * @property settings the shared preferences
 * @property editor the shared preferences editor
 */
object ManageStorage {
    private lateinit var settings: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    /**
     * Initializes the shared preferences and editor.
     *
     * @param settings the shared preferences
     * @param editor the shared preferences editor
     */
    fun init(settings: SharedPreferences, editor: SharedPreferences.Editor) {
        this.settings = settings
        this.editor = editor
    }

    /**
     * Adds a task to the recent tasks list, and saves the list to the shared preferences.
     *
     * @param task the task to add
     * @param option the option chosen for the task
     */
    fun addRecentTask(task: Task, option: String) {
        val timestamp: Long = System.currentTimeMillis()
        val storedTask = StoredTask(task.id, option, timestamp)
        val recentTasks = jsonToAlStoredTask(settings.getString("allRecentTasks", "[]")!!)
        recentTasks.add(0, storedTask)
        saveStoredTask(editor, "allRecentTasks", recentTasks)
    }
}