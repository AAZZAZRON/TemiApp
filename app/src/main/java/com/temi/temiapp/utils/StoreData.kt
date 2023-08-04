package com.temi.temiapp.utils

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

fun convertArrayListToJson(arrayList: ArrayList<Int>): String {
    return Gson().toJson(arrayList)
}

fun convertJsonToArrayList(json: String): ArrayList<Int> {
    Log.d("convertJsonToArrayList", json)
    val type: Type = object : TypeToken<ArrayList<Int>>() {}.type
    return Gson().fromJson(json, type)
}


fun saveTasks(editor: SharedPreferences.Editor, id: String, tasks: ArrayList<Task>) {
    val ids: ArrayList<Int> = ArrayList(tasks.map { it.id })
    val json = convertArrayListToJson(ids)
    Log.d("saveTasks", json)
    editor.putString(id, json)
    editor.apply()
}
