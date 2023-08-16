package com.temi.temiapp.utils

import android.widget.ProgressBar
import com.temi.temiapp.R
import com.temi.temiapp.ui.home.CurrentTasksAdapter
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

data class Task(val name: String, val description: String, val icon: Int, val runTask: suspend(updateProgress: (Int) -> Unit) -> Unit) {
    companion object {
        private val count: AtomicInteger = AtomicInteger(0) // ids start at 1
    }
    var id: Int = -1
    var isPinned: Boolean = true

    fun addExtraFields():Task {
        id = count.incrementAndGet()
        return this
    }
}

data class StoredTask(val task: Int, val timestamp: Long)
data class CompletedTask(val task: Task, val timestamp: Long)
data class CurrentTask(val task: Task, var progress: Int)


//@OptIn(DelicateCoroutinesApi::class)
//fun tempRunTask() = CoroutineScope(Dispatchers.Default).launch {
//    // create a new thread
//    println("Running task...")
//    delay(3000)
//}

// when implementing task runs, there would be a function to incrementProgressBar to show progress
suspend fun tempRunTask(updateProgress: (Int) -> Unit) {
    // create a new thread
    val seconds = 20//Random.nextInt(3, 10)
    for (i in 0..seconds) {
        delay(1000)
        updateProgress(i * 100 / seconds)
    }
}

val ALL_TASKS = mutableListOf<Task>(
    Task("Call Mom", "Description 1", R.drawable.ic_menu_camera) { updateProgress -> tempRunTask(updateProgress) }.addExtraFields(),
//    Task("Call to Lunch", "Description 2", R.drawable.ic_menu_task_history) { progressBar -> tempRunTask(progressBar) }.addExtraFields(),
//    Task("Call to Dinner", "Description 3", R.drawable.ic_menu_slideshow) { progressBar -> tempRunTask(progressBar) }.addExtraFields(),
//    Task("Buy Toilet Paper", "Description 4", R.drawable.ic_menu_camera) { progressBar -> tempRunTask(progressBar) }.addExtraFields(),
//    Task("Text Bobby", "Description 1", R.drawable.ic_menu_camera) { progressBar -> tempRunTask(progressBar) }.addExtraFields(),
//    Task("Order Pizza", "Description 2", R.drawable.ic_menu_task_history) { progressBar -> tempRunTask(progressBar) }.addExtraFields(),
//    Task("Deliver Meds", "Description 3", R.drawable.ic_menu_slideshow) { progressBar -> tempRunTask(progressBar) }.addExtraFields(),
//    Task("Sing a Song", "Description 4", R.drawable.ic_menu_camera) { progressBar -> tempRunTask(progressBar) }.addExtraFields(),
) as ArrayList<Task>
