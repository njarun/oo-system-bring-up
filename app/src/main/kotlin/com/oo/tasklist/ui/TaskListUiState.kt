package com.oo.tasklist.ui

import com.oo.tasklist.data.Task

sealed interface TaskListUiState {
    data object Loading : TaskListUiState
    data object Empty : TaskListUiState
    data class Loaded(
        val tasks: List<Task>,
        val remainingCount: Int,
    ) : TaskListUiState
}
