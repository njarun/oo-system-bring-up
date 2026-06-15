# Test Report Summary — :app:testDebugUnitTest

**Toolchain:** JDK 25 · Kotlin 2.3 · Gradle 9.4 · Robolectric 4.16  
**Run date:** 2026-06-15  
**Task:** `:app:testDebugUnitTest`

## Counts

| Result  | Count |
|---------|-------|
| Passed  | 14    |
| Failed  | 0     |
| Skipped | 0     |
| **Total** | **14** |

**Success rate:** 100%  
**Duration:** 22.8 s

## Per-Class Digest

### CountsTest (3 / 3 passed)
- `empty list returns 0`
- `all done returns 0`
- `mixed list returns not-done count`

### InMemoryTaskRepositoryTest (4 / 4 passed)
- `starts empty`
- `add appends a task`
- `toggle flips done state`
- `ordering preserved across mutations`

### TaskListViewModelTest (4 / 4 passed)
- `initial value is Loading before any subscriber attaches`
- `subscribing to empty repo yields Empty state`
- `onAdd creates a not-done task and remainingCount is 1`
- `onToggle marks task done and remainingCount decrements to 0`

### TaskListScreenTest (3 / 3 passed) — Robolectric + Compose UI
- `empty state shows placeholder text`
- `added task appears in the list`
- `count header reflects number of active tasks`

## Roborazzi

Record mode is enabled in `TaskListScreenTest` via `@get:Rule val roborazziRule = RoborazziRule(...)`.  
No reference images are committed yet; `verifyRoborazziDemoDebug` is **not** wired into the gate on this run (see OPE role instructions — record-only for first pass).

## Report

Full interactive HTML report: [`index.html`](index.html)
