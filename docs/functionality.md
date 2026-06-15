# OO Task List — Functionality Report

## What it is

OO Task List is a single-screen Android app that lets you capture short tasks and
tick them off. It was built end-to-end as the greenfield delivery for Operation
Outbound: a Kotlin + Jetpack Compose vertical slice — UI, ViewModel, Repository,
DI, and tests — running on Android API 24+. There is no account, no sync, and no
server; everything lives in process memory for the lifetime of the app.

## What it does

The app has exactly one screen and renders one of three states:

- **Loading** — shown for the brief window before the repository emits its first
  list. A centered circular progress indicator fills the screen above the
  add-task row.
- **Empty** — shown when the repository has emitted but the task list is empty
  (the initial state on a cold start). The body reads
  `No tasks yet — add one below!`.
- **Loaded** — shown as soon as at least one task exists. The header line shows
  `N remaining`, where N is the count of tasks whose `done` flag is `false`. Each
  task renders as a row with a checkbox and the task text.

The add-task row is always visible at the bottom of the screen, regardless of
state:

- A single-line `OutlinedTextField` with the placeholder `New task…`.
- An `Add` button next to it. Submitting an entry trims whitespace; blank or
  whitespace-only input is ignored. On success the field clears.
- The text field is backed by `rememberSaveable`, so an in-progress draft
  survives configuration changes (e.g. rotation).

Toggle behaviour:

- Tapping a row's checkbox flips that task's `done` flag.
- The header's remaining count updates immediately on the next emission.
- Toggling does not reorder the list — tasks stay in the order they were added.

Persistence model:

- The single source of truth is `InMemoryTaskRepository`, a `@Singleton`-scoped
  Hilt binding that holds a `MutableStateFlow<List<Task>>` initialised to
  `emptyList()`.
- Tasks are **not** persisted to disk. Killing the process — swiping the app off
  the recents tray, an OS-driven kill, or reinstalling — drops the list back to
  empty. Configuration changes (rotation, theme switch) preserve the list because
  the repository outlives the activity.

## How to use it

1. Launch **OO Task List** from the launcher. You will land directly on the task
   list screen — there is no splash or onboarding.
2. On first launch you will see the empty state.
3. Type a task into the `New task…` field at the bottom of the screen.
4. Tap **Add**. The task appears in the list above and the input clears. The
   header now reads `1 remaining`.
5. Add as many tasks as you like; each new task is appended to the bottom of the
   list.
6. Tap a row's checkbox to mark a task done. The checkbox fills in and the
   header's remaining count drops by one. Tap it again to flip the task back to
   not-done.
7. To start over, close and relaunch the app; the in-memory store will be empty
   again.

There is no delete action, no edit action, no filter, no sort, no priority, and
no due-date in this slice. The acceptance scope is exactly the three states, the
add input, and the toggle — see [OPE-45](/OPE/issues/OPE-45) DoD.

## Architecture (one paragraph)

The app is a UI → ViewModel → Repository slice wired with Hilt. `MainActivity`
is an `@AndroidEntryPoint` `ComponentActivity` that calls `setContent { ... }`
into `AppNavGraph`, a single-destination `NavHost` whose only route is `tasks`
(`com.oo.tasklist.nav.ROUTE_TASKS`). The route renders `TaskListScreen`, which
obtains a `TaskListViewModel` via `hiltViewModel()` and forwards `onAdd` /
`onToggle` callbacks to a stateless `TaskListContent` composable that owns the
three-state rendering and the add-task row. `TaskListViewModel` is
`@HiltViewModel`-injected with a `TaskRepository`, maps `repository.observe()`
into `TaskListUiState` (`Loading` → `Empty` → `Loaded(tasks, remainingCount)`),
and exposes the resulting `StateFlow` via `stateIn(viewModelScope,
SharingStarted.WhileSubscribed(5_000), Loading)`. The repository contract
(`TaskRepository`) is bound by `DataModule` to `InMemoryTaskRepository`, a
`@Singleton` that owns a `MutableStateFlow<List<Task>>` and serves `observe()`,
`add(text)`, and `toggle(id)`; there is no DAO, no network, no DataStore.

## Pointers

- App entry point: `app/src/main/kotlin/com/oo/tasklist/MainActivity.kt`
- Nav graph: `app/src/main/kotlin/com/oo/tasklist/nav/NavGraph.kt`
- Screen / composables: `app/src/main/kotlin/com/oo/tasklist/ui/TaskListScreen.kt`
- ViewModel: `app/src/main/kotlin/com/oo/tasklist/ui/TaskListViewModel.kt`
- UI state: `app/src/main/kotlin/com/oo/tasklist/ui/TaskListUiState.kt`
- Repository contract: `app/src/main/kotlin/com/oo/tasklist/data/TaskRepository.kt`
- In-memory store: `app/src/main/kotlin/com/oo/tasklist/data/InMemoryTaskRepository.kt`
- Hilt binding: `app/src/main/kotlin/com/oo/tasklist/data/DataModule.kt`
- Strings: `app/src/main/res/values/strings.xml`
