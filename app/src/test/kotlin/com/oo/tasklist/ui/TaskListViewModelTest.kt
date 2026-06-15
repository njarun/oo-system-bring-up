package com.oo.tasklist.ui

import com.oo.tasklist.data.FakeTaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskListViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun makeViewModel(repo: FakeTaskRepository = FakeTaskRepository()) =
        TaskListViewModel(repo)

    @Test
    fun `initial value is Loading before any subscriber attaches`() {
        val viewModel = makeViewModel()
        assertEquals(TaskListUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `subscribing to empty repo yields Empty state`() = runTest(testDispatcher) {
        val viewModel = makeViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }
        advanceUntilIdle()
        assertEquals(TaskListUiState.Empty, viewModel.uiState.value)
    }

    @Test
    fun `onAdd creates a not-done task and remainingCount is 1`() = runTest(testDispatcher) {
        val viewModel = makeViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }
        advanceUntilIdle()

        viewModel.onAdd("buy milk")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is TaskListUiState.Loaded)
        state as TaskListUiState.Loaded
        assertEquals(1, state.tasks.size)
        assertEquals("buy milk", state.tasks[0].text)
        assertFalse(state.tasks[0].done)
        assertEquals(1, state.remainingCount)
    }

    @Test
    fun `onToggle marks task done and remainingCount decrements to 0`() = runTest(testDispatcher) {
        val viewModel = makeViewModel()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }
        advanceUntilIdle()

        viewModel.onAdd("buy milk")
        advanceUntilIdle()

        val id = (viewModel.uiState.value as TaskListUiState.Loaded).tasks[0].id
        viewModel.onToggle(id)
        advanceUntilIdle()

        val state = viewModel.uiState.value as TaskListUiState.Loaded
        assertTrue(state.tasks[0].done)
        assertEquals(0, state.remainingCount)
    }
}
