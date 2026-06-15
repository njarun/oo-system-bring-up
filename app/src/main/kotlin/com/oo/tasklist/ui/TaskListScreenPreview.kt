package com.oo.tasklist.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.oo.tasklist.data.Task

@Preview(name = "Empty state", showBackground = true)
@Composable
fun TaskListScreenEmptyPreview() {
    TaskListContent(
        uiState = TaskListUiState.Empty,
        onAddTask = {},
        onToggleTask = {},
    )
}

@Preview(name = "Populated", showBackground = true)
@Composable
fun TaskListScreenPopulatedPreview() {
    TaskListContent(
        uiState = TaskListUiState.Loaded(
            tasks = listOf(
                Task(id = "1", text = "Buy groceries", done = true),
                Task(id = "2", text = "Write tests", done = false),
                Task(id = "3", text = "Ship the app", done = false),
            ),
            remainingCount = 2,
        ),
        onAddTask = {},
        onToggleTask = {},
    )
}
