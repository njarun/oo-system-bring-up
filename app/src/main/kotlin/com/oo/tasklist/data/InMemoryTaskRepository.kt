package com.oo.tasklist.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryTaskRepository @Inject constructor() : TaskRepository {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())

    override fun observe(): Flow<List<Task>> = _tasks.asStateFlow()

    override fun add(text: String) {
        val task = Task(id = UUID.randomUUID().toString(), text = text, done = false)
        _tasks.update { it + task }
    }

    override fun toggle(id: String) {
        _tasks.update { tasks ->
            tasks.map { if (it.id == id) it.copy(done = !it.done) else it }
        }
    }
}
