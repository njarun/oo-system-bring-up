package com.oo.tasklist.data

import kotlin.test.assertEquals
import org.junit.Test

class CountsTest {

    @Test
    fun `empty list returns 0`() {
        assertEquals(0, Counts.remainingCount(emptyList()))
    }

    @Test
    fun `all done returns 0`() {
        val tasks = listOf(
            Task("1", "wash dishes", done = true),
            Task("2", "clean room", done = true),
        )
        assertEquals(0, Counts.remainingCount(tasks))
    }

    @Test
    fun `mixed list returns not-done count`() {
        val tasks = listOf(
            Task("1", "buy milk", done = false),
            Task("2", "read book", done = true),
            Task("3", "call mom", done = false),
        )
        assertEquals(2, Counts.remainingCount(tasks))
    }
}
