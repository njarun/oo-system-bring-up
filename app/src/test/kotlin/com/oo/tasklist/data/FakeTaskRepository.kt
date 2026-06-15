package com.oo.tasklist.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeTaskRepository : TaskRepository {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    private var nextId = 1

    override fun observe(): Flow<List<Task>> = _tasks

    override fun add(text: String) {
        _tasks.value = _tasks.value + Task(
            id = (nextId++).toString(),
            text = text,
            done = false,
        )
    }

    override fun toggle(id: String) {
        _tasks.value = _tasks.value.map { task ->
            if (task.id == id) task.copy(done = !task.done) else task
        }
    }
}
