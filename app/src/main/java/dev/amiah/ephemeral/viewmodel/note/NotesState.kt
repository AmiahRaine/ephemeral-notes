package dev.amiah.ephemeral.viewmodel.note

import dev.amiah.ephemeral.data.entity.NoteWithTasks
import dev.amiah.ephemeral.data.entity.Task

data class NotesState (
    val notes: List<NoteWithTasks> = emptyList(),
    val isAdding: Boolean = false
)