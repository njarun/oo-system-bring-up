package com.oo.tasklist.data

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.Test

class InMemoryTaskRepositoryTest {

    @Test
    fun `starts empty`() = runTest {
        val repo = InMemoryTaskRepository()
        assertTrue(repo.observe().first().isEmpty())
    }

    @Test
    fun `add appends a task`() = runTest {
        val repo = InMemoryTaskRepository()
        repo.add("buy milk")
        val tasks = repo.observe().first()
        assertEquals(1, tasks.size)
        assertEquals("buy milk", tasks[0].text)
        assertFalse(tasks[0].done)
    }

    @Test
    fun `toggle flips done state`() = runTest {
        val repo = InMemoryTaskRepository()
        repo.add("buy milk")
        val id = repo.observe().first()[0].id
        repo.toggle(id)
        assertTrue(repo.observe().first()[0].done)
        repo.toggle(id)
        assertFalse(repo.observe().first()[0].done)
    }

    @Test
    fun `ordering preserved across mutations`() = runTest {
        val repo = InMemoryTaskRepository()
        repo.add("first")
        repo.add("second")
        repo.add("third")
        val tasks = repo.observe().first()
        assertEquals(listOf("first", "second", "third"), tasks.map { it.text })
        repo.toggle(tasks[1].id)
        val after = repo.observe().first()
        assertEquals(listOf("first", "second", "third"), after.map { it.text })
    }
}
