package com.temi.temiapp.utils

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Converts an ArrayList of Ints to a JSON string.
 * @return JSON string
 */
fun alIntToJson(arrayList: ArrayList<Int>): String {
    return Gson().toJson(arrayList)
}

/**
 * Converts an ArrayList of StoredTasks to a JSON string.
 * @return JSON string
 */
fun alStoredTaskToJson(arrayList: ArrayList<StoredTask>): String {
    return Gson().toJson(arrayList)
}

/**
 * Converts a JSON string to an ArrayList of Tasks.
 * @return ArrayList of Tasks
 */
fun jsonToAlTask(json: String): ArrayList<Task> {
    Log.d("Task", json)
    val type: Type = object : TypeToken<ArrayList<Int>>() {}.type
    val tmp = Gson().fromJson<ArrayList<Int>>(json, type)
    return ALL_TASKS.filter { tmp.contains(it.id) } as ArrayList<Task>
}

/**
 * Converts a JSON string to an ArrayList of StoredTasks.
 * @return ArrayList of StoredTasks
 */
fun jsonToAlStoredTask(json: String): ArrayList<StoredTask> {
    Log.d("CompletedTask", json)
    val type: Type = object : TypeToken<ArrayList<StoredTask>>() {}.type
    return Gson().fromJson(json, type)
}

/**
 * Saves an ArrayList of StoredTasks to the shared preferences.
 * @param editor the shared preferences editor
 * @param id the id of the shared preferences to save to
 * @param tasks the ArrayList of StoredTasks to save
 */
fun saveStoredTask(editor: SharedPreferences.Editor, id: String, tasks: ArrayList<StoredTask>) {
    val json = alStoredTaskToJson(tasks)
    Log.d("saveTasks", json)
    editor.putString(id, json)
    editor.apply()
}

