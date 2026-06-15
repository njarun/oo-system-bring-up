package com.oo.tasklist.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.oo.tasklist.data.FakeTaskRepository
import com.github.takahirom.roborazzi.captureRoboImage
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
        composeRule.waitForIdle()
        composeRule.onNodeWithText("No tasks yet", substring = true).assertIsDisplayed()
        composeRule.onRoot().captureRoboImage("../screenshots/task_list_empty.png")
    }

    @Test
    fun `added task appears in the list`() {
        composeRule.setContent {
            MaterialTheme {
                TaskListScreen(viewModel = viewModel)
            }
        }
        viewModel.onAdd("buy milk")
        composeRule.waitForIdle()
        composeRule.onNodeWithText("buy milk").assertIsDisplayed()
        composeRule.onRoot().captureRoboImage("../screenshots/task_list_one_task.png")
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
        composeRule.waitForIdle()
        // Header must display the remaining count (2 undone tasks).
        composeRule.onNodeWithText("2", substring = true).assertIsDisplayed()
        composeRule.onRoot().captureRoboImage("../screenshots/task_list_two_tasks.png")
    }

    @Test
    fun `loaded state with mixed done and not-done tasks shows count`() {
        composeRule.setContent {
            MaterialTheme {
                TaskListScreen(viewModel = viewModel)
            }
        }
        viewModel.onAdd("buy milk")
        viewModel.onAdd("buy eggs")
        composeRule.waitForIdle()
        // FakeTaskRepository assigns sequential IDs starting at "1"
        viewModel.onToggle("1")
        composeRule.waitForIdle()
        // "buy milk" is done; "buy eggs" is pending — count shows 1 remaining
        composeRule.onNodeWithText("1", substring = true).assertIsDisplayed()
        composeRule.onRoot().captureRoboImage("../screenshots/task_list_mixed.png")
    }
}
