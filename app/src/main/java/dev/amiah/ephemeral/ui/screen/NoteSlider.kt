package dev.amiah.ephemeral.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import dev.amiah.ephemeral.data.entity.Task
import dev.amiah.ephemeral.viewmodel.note.NotesEvent
import dev.amiah.ephemeral.viewmodel.note.NotesState
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
fun NoteSlider(notesState: NotesState?, onEvent: (NotesEvent) -> Unit) {

    //TODO: IF NO PAGES ADD ONE
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
            Column(modifier = Modifier.padding(vertical = 5.dp, horizontal = 8.dp)) {
                Text(text = "I am number $pageNumber.")
                notesState?.notes?.get(pageNumber)?.tasks?.forEach { task ->
                    TaskEntry(task, onEvent)
                }

            }
            //onEvent(NotesEvent.SaveNote)
        }
    }
}

@Composable
fun TaskEntry(task: Task, onEvent: (NotesEvent) -> Unit) {


    Row() {
        Checkbox(checked = task.isDone, onCheckedChange = {
            task.isDone = it;
            onEvent(NotesEvent.SaveTask(task))
        })

        TextField(state = rememberTextFieldState())
    }
}