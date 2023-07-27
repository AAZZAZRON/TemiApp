package com.temi.temiapp.utils

import com.temi.temiapp.R
import com.temi.temiapp.ui.home.CurrentTasksAdapter
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger

data class Task(val name: String, val description: String, val icon: Int, val runTask: suspend() -> Unit) {
    companion object {
        private val count: AtomicInteger = AtomicInteger(0) // ids start at 1
    }
    var id: Number = -1
    var isPinned: Boolean = true

    fun addExtraFields():Task {
        id = count.incrementAndGet()
        return this
    }

    suspend fun executeTask(adapter: CurrentTasksAdapter?) {
        withContext(Dispatchers.Default) {
            runTask()
        }
        adapter?.removeTask(this)
    }
}

//@OptIn(DelicateCoroutinesApi::class)
//fun tempRunTask() = CoroutineScope(Dispatchers.Default).launch {
//    // create a new thread
//    println("Running task...")
//    delay(3000)
//}

suspend fun tempRunTask() {
    // create a new thread
    println("Running task...")
    delay(3000)
}

val ALL_TASKS = mutableListOf<Task>(
//    Task("Call Mom", "Description 1", R.drawable.ic_menu_camera) { dispatcher -> tempRunTask(dispatcher) }.addExtraFields(),
//    Task("Call to Lunch", "Description 2", R.drawable.ic_menu_gallery) { dispatcher -> tempRunTask(dispatcher) }.addExtraFields(),
//    Task("Call to Dinner", "Description 3", R.drawable.ic_menu_slideshow) { dispatcher -> tempRunTask(dispatcher) }.addExtraFields(),
    Task("Buy Toilet Paper", "Description 4", R.drawable.ic_menu_camera) { tempRunTask() }.addExtraFields(),
    Task("Text Bobby", "Description 1", R.drawable.ic_menu_camera) { tempRunTask() }.addExtraFields(),
    Task("Order Pizza", "Description 2", R.drawable.ic_menu_gallery) { tempRunTask() }.addExtraFields(),
//    Task("Deliver Meds", "Description 3", R.drawable.ic_menu_slideshow) { tempRunTask() }.addExtraFields(),
//    Task("Sing a Song", "Description 4", R.drawable.ic_menu_camera) { tempRunTask() }.addExtraFields(),
) as ArrayList<Task>
