package com.temi.temiapp.utils

import com.temi.temiapp.R
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random


/**
 * Data classes for tasks.
 * Template to create a task
 * @param name the name of the task
 * @param description the description of the task
 * @param icon the icon of the task
 * @param runTask the function to run when the task is started
 */
data class Task(var name: String, val description: String, val icon: Int, val runTask: suspend(updateProgress: (Int) -> Unit) -> Unit) {
    companion object {
        /** generates unique ids for tasks */
        private val count: AtomicInteger = AtomicInteger(0) // ids start at 1
    }
    var id: Int = -1
    var isPinned: Boolean = true

    /** the list of options (specifications) for the task */
    var specs: ArrayList<TaskSpec> = ArrayList()

    /** adds custom fields to the task */
    fun addExtraFields():Task {
        id = count.incrementAndGet()
        specs.add(TaskSpec("Option A"))
        specs.add(TaskSpec("Option B"))
        specs.add(TaskSpec("Option C"))
        return this
    }
}


/**
 * Data classes for tasks.
 * Stores the task as an ID in persistent storage
 * @see CompletedTask
 */
data class StoredTask(val task: Int, val option: String, val timestamp: Long)

/**
 * Data classes for tasks.
 * This is how completed tasks are stored in the app (when its running)
 */
data class CompletedTask(val task: Task, val option: String, val timestamp: Long)

/**
 * Data classes for tasks.
 * Keeps track of the current tasks that are running and their progress
 * @param task the task
 * @param option the option (specification) for the task
 * @param progress the progress of the task (0-100)
 * @see BackgroundTasks.executeTask
 */
data class CurrentTask(val task: Task, val option: String, var progress: Int)

/**
 * Data classes for tasks.
 * @param option the option (specification) for the task
 * @param checked whether the option is checked (selected)
 */
data class TaskSpec(val option: String, var checked: Boolean = false)



// when implementing task runs, there would be a function to incrementProgressBar to show progress
suspend fun tempRunTask(updateProgress: (Int) -> Unit) {
    // create a new thread
    val seconds = Random.nextInt(5, 20)
    for (i in 0..seconds) {
        delay(1000)
        updateProgress(i * 100 / seconds)
    }
}

/**
 * The list of all tasks
 */
val ALL_TASKS = mutableListOf<Task>(
    Task("Call Mom", "Description 1", R.drawable.ic_menu_camera) { updateProgress -> tempRunTask(updateProgress) }.addExtraFields(),
    Task("Call to Lunch", "Description 2", R.drawable.ic_menu_task_history) { updateProgress -> tempRunTask(updateProgress) }.addExtraFields(),
    Task("Call to Dinner", "Description 3", R.drawable.ic_menu_slideshow) { updateProgress -> tempRunTask(updateProgress) }.addExtraFields(),
    Task("Buy Toilet Paper", "Description 4", R.drawable.ic_menu_camera) { updateProgress -> tempRunTask(updateProgress) }.addExtraFields(),
    Task("Text Bobby", "Description 1", R.drawable.ic_menu_camera) { updateProgress -> tempRunTask(updateProgress) }.addExtraFields(),
    Task("Order Pizza", "Description 2", R.drawable.ic_menu_task_history) { updateProgress -> tempRunTask(updateProgress) }.addExtraFields(),
    Task("Deliver Meds", "Description 3", R.drawable.ic_menu_slideshow) { updateProgress -> tempRunTask(updateProgress) }.addExtraFields(),
    Task("Sing a Song", "Description 4", R.drawable.ic_menu_camera) { updateProgress -> tempRunTask(updateProgress) }.addExtraFields(),
) as ArrayList<Task>
