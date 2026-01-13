package dev.amiah.ephemeral.ui.element

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import dev.amiah.ephemeral.data.entity.Task
import dev.amiah.ephemeral.viewmodel.note.NoteEvent
import dev.amiah.ephemeral.viewmodel.note.NotesState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue

private val partialPageSize = object : PageSize {
    override fun Density.calculateMainAxisPageSize(
        availableSpace: Int,
        pageSpacing: Int
    ): Int {
        return (availableSpace - pageSpacing) / 2;
    }
}

@Composable
fun NoteSlider(notesState: NotesState?, onEvent: (NoteEvent) -> Unit) {

    val pagerState = rememberPagerState(pageCount = { notesState?.notes?.size ?: 0 })

    HorizontalPager(pagerState) { pageNumber ->
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .graphicsLayer {
                    // Offset of current page from scroll position
                    val pageOffset = ((pagerState.currentPage - pageNumber)
                            + pagerState.currentPageOffsetFraction).absoluteValue
                    // Interpolate alpha based on page offset
                    alpha = lerp(0.5f, 1f, 1f - pageOffset.coerceIn(0f, 1f))

                    // Interpolate size based on page offset
                    scaleX = lerp(1f, 0.9f, pageOffset.coerceIn(0f, 1f))
                    scaleY = scaleX
                }

        ) {
            // Task entries go in this column
            Column(modifier = Modifier
                .padding(vertical = 5.dp, horizontal = 8.dp)
            ) {
                // Day text at top of note
                Text(text = "${
                    notesState?.notes?.get(pageNumber)?.note?.time?.atZone(
                        ZoneId.systemDefault())?.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))
                }.")
                // Put task entries in a scroller
                Column(modifier = Modifier
                    .weight(0.8f)
                    .verticalScroll(rememberScrollState())
                ) {
                    notesState?.notes?.get(pageNumber)?.tasks?.forEach { task ->
                        TaskEntry(task, notesState, onEvent)
                    }
                }
                AddTaskEntryButton(notesState?.notes?.get(pageNumber)?.note?.id, onEvent)

            }
        }
    }
}

@Composable
fun TaskEntry(task: Task, notesState: NotesState?, onEvent: (NoteEvent) -> Unit) {

    Row() {
        Checkbox(checked = task.isDone, onCheckedChange = {
            onEvent(NoteEvent.SaveTaskIsDone(task, isDone = it))
        })

        // Make a box that can be clicked. Holds a Text until clicked then displays a text edit.
        Box(modifier = Modifier.fillMaxWidth().heightIn(min=69.dp)
            .padding(PaddingValues(top = 5.dp, bottom = 5.dp, end = 10.dp, start = 3.dp))
            .clickable(onClick = { onEvent(NoteEvent.SwitchCurrentTask(task)) } )
        ) {
            // Switch to text field if it is the current task
            if (notesState?.currentTask?.id == task.id) {
                val focusRequester = remember { FocusRequester() }
                val scope = rememberCoroutineScope()
                val isFocused = remember { mutableStateOf(true) }

                TextField(value = notesState.currentTaskText,
                    modifier = Modifier.fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            // Unselect task when focus is lost
                            isFocused.value = it.isFocused
                            scope.launch {
                                delay(50)
                                if (!isFocused.value) {
                                    onEvent(NoteEvent.SwitchCurrentTask(null))
                                }
                            }
                        },
                    onValueChange = {
                        onEvent(NoteEvent.ModifyTaskTextFieldValue(it))
                        onEvent(NoteEvent.SaveTaskText(task.copy(text = it.text), it))
                    }
                )

                LaunchedEffect(Unit) { focusRequester.requestFocus(); }
            }
            else {
                Text(text = task.text, modifier = Modifier.padding(16.dp))
            }
        }

    }
}

@Composable
fun AddTaskEntryButton(parentId: Long?, onEvent: (NoteEvent) -> Unit) {
    Button(onClick = {
        onEvent(NoteEvent.CreateTask(parentId ?: -1))
    }) { Text("NEW") }
}