package com.oo.tasklist.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.oo.tasklist.data.FakeTaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// Runs on JVM via Robolectric (sdk pinned in robolectric.properties to 33).
// TaskListScreen must accept an explicit viewModel parameter for testability:
//   @Composable fun TaskListScreen(viewModel: TaskListViewModel = hiltViewModel())
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class TaskListScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val fakeRepo = FakeTaskRepository()
    private val viewModel = TaskListViewModel(fakeRepo)

    @Test
    fun `empty state shows placeholder text`() {
        composeRule.setContent {
            MaterialTheme {
                TaskListScreen(viewModel = viewModel)
            }
        }
        composeRule.onNodeWithText("No tasks yet", substring = true).assertIsDisplayed()
    }

    @Test
    fun `added task appears in the list`() {
        composeRule.setContent {
            MaterialTheme {
                TaskListScreen(viewModel = viewModel)
            }
        }
        viewModel.onAdd("buy milk")
        composeRule.onNodeWithText("buy milk").assertIsDisplayed()
    }

    @Test
    fun `count header reflects number of active tasks`() {
        composeRule.setContent {
            MaterialTheme {
                TaskListScreen(viewModel = viewModel)
            }
        }
        viewModel.onAdd("task one")
        viewModel.onAdd("task two")
        // Header must display the remaining count (2 undone tasks).
        composeRule.onNodeWithText("2", substring = true).assertIsDisplayed()
    }
}
