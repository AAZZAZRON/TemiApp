package com.temi.temiapp.utils

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

// arraylist int to json
fun alIntToJson(arrayList: ArrayList<Int>): String {
    return Gson().toJson(arrayList)
}

fun alStoredCompletedTaskToJson(arrayList: ArrayList<StoredCompletedTask>): String {
    return Gson().toJson(arrayList)
}

fun jsonToAlTask(json: String): ArrayList<Task> {
    Log.d("Task", json)
    val type: Type = object : TypeToken<ArrayList<Int>>() {}.type
    val tmp = Gson().fromJson<ArrayList<Int>>(json, type)
    return ALL_TASKS.filter { tmp.contains(it.id) } as ArrayList<Task>
}

fun jsonToAlStoredCompletedTask(json: String): ArrayList<StoredCompletedTask> {
    Log.d("CompletedTask", json)
    val type: Type = object : TypeToken<ArrayList<StoredCompletedTask>>() {}.type
    return Gson().fromJson(json, type)
}

// overloaded
fun saveTask(editor: SharedPreferences.Editor, id: String, tasks: ArrayList<Task>) {
    val ids: ArrayList<Int> = ArrayList(tasks.map { it.id })
    val json = alIntToJson(ids)
    Log.d("saveTasks", json)
    editor.putString(id, json)
    editor.apply()
}

fun saveStoredCompletedTask(editor: SharedPreferences.Editor, id: String, tasks: ArrayList<StoredCompletedTask>) {
    val json = alStoredCompletedTaskToJson(tasks)
    Log.d("saveTasks", json)
    editor.putString(id, json)
    editor.apply()
}

