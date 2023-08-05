package com.temi.temiapp.utils

import android.widget.ProgressBar
import com.temi.temiapp.R
import com.temi.temiapp.ui.home.CurrentTasksAdapter
import kotlinx.coroutines.*
import java.sql.Timestamp
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

data class Task(val name: String, val description: String, val icon: Int, val runTask: suspend(ProgressBar) -> Unit) {
    companion object {
        private val count: AtomicInteger = AtomicInteger(0) // ids start at 1
    }
    var id: Int = -1
    var isPinned: Boolean = true

    fun addExtraFields():Task {
        id = count.incrementAndGet()
        return this
    }

    suspend fun executeTask(adapter: CurrentTasksAdapter?, progressBar: ProgressBar) {
        withContext(Dispatchers.Default) {
            runTask(progressBar)
        }
        adapter?.removeTask(this)
    }
}

data class StoredCompletedTask(val task: Int, val timestamp: Long)
data class CompletedTask(val task: Task, val timestamp: Long)


//@OptIn(DelicateCoroutinesApi::class)
//fun tempRunTask() = CoroutineScope(Dispatchers.Default).launch {
//    // create a new thread
//    println("Running task...")
//    delay(3000)
//}

// when implementing task runs, there would be a function to incrementProgressBar to show progress
suspend fun tempRunTask(progressBar: ProgressBar) {
    // create a new thread
    val seconds = Random.nextInt(3, 10)
    for (i in 0..seconds) {
        progressBar.post {
            progressBar.progress = (i * 100) / seconds
        }
        delay(1000)
    }
}

val ALL_TASKS = mutableListOf<Task>(
    Task("Call Mom", "Description 1", R.drawable.ic_menu_camera) { progressBar -> tempRunTask(progressBar) }.addExtraFields(),
    Task("Call to Lunch", "Description 2", R.drawable.ic_menu_gallery) { progressBar -> tempRunTask(progressBar) }.addExtraFields(),
    Task("Call to Dinner", "Description 3", R.drawable.ic_menu_slideshow) { progressBar -> tempRunTask(progressBar) }.addExtraFields(),
    Task("Buy Toilet Paper", "Description 4", R.drawable.ic_menu_camera) { progressBar -> tempRunTask(progressBar) }.addExtraFields(),
    Task("Text Bobby", "Description 1", R.drawable.ic_menu_camera) { progressBar -> tempRunTask(progressBar) }.addExtraFields(),
    Task("Order Pizza", "Description 2", R.drawable.ic_menu_gallery) { progressBar -> tempRunTask(progressBar) }.addExtraFields(),
    Task("Deliver Meds", "Description 3", R.drawable.ic_menu_slideshow) { progressBar -> tempRunTask(progressBar) }.addExtraFields(),
    Task("Sing a Song", "Description 4", R.drawable.ic_menu_camera) { progressBar -> tempRunTask(progressBar) }.addExtraFields(),
) as ArrayList<Task>
