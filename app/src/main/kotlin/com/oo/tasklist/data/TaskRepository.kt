package com.oo.tasklist.data

import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun observe(): Flow<List<Task>>
    fun add(text: String)
    fun toggle(id: String)
}
