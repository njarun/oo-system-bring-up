package com.oo.tasklist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oo.tasklist.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val repository: TaskRepository,
) : ViewModel() {

    val uiState: StateFlow<TaskListUiState> = repository.observe()
        .map { tasks ->
            if (tasks.isEmpty()) TaskListUiState.Empty
            else TaskListUiState.Loaded(
                tasks = tasks,
                remainingCount = tasks.count { !it.done },
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TaskListUiState.Loading,
        )

    fun onAdd(text: String) = repository.add(text)

    fun onToggle(id: String) = repository.toggle(id)
}
