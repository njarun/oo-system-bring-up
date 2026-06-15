package com.oo.tasklist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oo.tasklist.R
import com.oo.tasklist.data.Task

@Composable
fun TaskListScreen(viewModel: TaskListViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TaskListContent(
        uiState = uiState,
        onAddTask = viewModel::onAdd,
        onToggleTask = viewModel::onToggle,
    )
}

@Composable
fun TaskListContent(
    uiState: TaskListUiState,
    onAddTask: (String) -> Unit,
    onToggleTask: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var inputText by rememberSaveable { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            TaskListUiState.Loading -> {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            TaskListUiState.Empty -> {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(R.string.empty_state))
                }
            }

            is TaskListUiState.Loaded -> {
                Text(
                    text = "${uiState.remainingCount} remaining",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                )
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(uiState.tasks, key = { it.id }) { task ->
                        TaskRow(task = task, onToggle = { onToggleTask(task.id) })
                    }
                }
            }
        }

        AddTaskRow(
            value = inputText,
            onValueChange = { inputText = it },
            onAdd = {
                val trimmed = inputText.trim()
                if (trimmed.isNotEmpty()) {
                    onAddTask(trimmed)
                    inputText = ""
                }
            },
        )
    }
}

@Composable
private fun TaskRow(task: Task, onToggle: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = task.done,
            onCheckedChange = { onToggle() },
        )
        Text(
            text = task.text,
            modifier = Modifier.weight(1f).padding(start = 8.dp),
        )
    }
}

@Composable
private fun AddTaskRow(
    value: String,
    onValueChange: (String) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(stringResource(R.string.add_task_hint)) },
            modifier = Modifier.weight(1f),
            singleLine = true,
        )
        OutlinedButton(onClick = onAdd) {
            Text(stringResource(R.string.add_task_button))
        }
    }
}
