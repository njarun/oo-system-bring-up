package com.oo.tasklist.data

fun remainingCount(tasks: List<Task>): Int = tasks.count { !it.done }
