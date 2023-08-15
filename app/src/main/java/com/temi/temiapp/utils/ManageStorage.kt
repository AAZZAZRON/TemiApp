package com.temi.temiapp.utils

import android.content.SharedPreferences

object ManageStorage {
    private lateinit var settings: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    fun init(settings: SharedPreferences, editor: SharedPreferences.Editor) {
        this.settings = settings
        this.editor = editor
    }

    fun addRecentTask(task: Task) {
        val timestamp: Long = System.currentTimeMillis()
        val storedTask = StoredTask(task.id, timestamp)
        val recentTasks = jsonToAlStoredTask(settings.getString("allRecentTasks", "[]")!!)
        recentTasks.add(0, storedTask)
        saveStoredTask(editor, "allRecentTasks", recentTasks)
    }
}