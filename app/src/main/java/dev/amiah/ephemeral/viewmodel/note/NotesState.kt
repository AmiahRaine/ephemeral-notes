package dev.amiah.ephemeral.viewmodel.note

import androidx.compose.ui.text.input.TextFieldValue
import dev.amiah.ephemeral.data.entity.NoteWithTasks
import dev.amiah.ephemeral.data.entity.Task

data class NotesState (
    val notes: List<NoteWithTasks> = emptyList(),
    val currentTask: Task? = null,
    val currentTaskText: TextFieldValue = TextFieldValue(),
    val currentTaskIsNew: Boolean = false
)