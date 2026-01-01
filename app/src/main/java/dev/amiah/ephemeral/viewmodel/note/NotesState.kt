package dev.amiah.ephemeral.viewmodel.note

import dev.amiah.ephemeral.data.entity.NoteWithTasks
import dev.amiah.ephemeral.viewmodel.task.TaskState

data class NotesState (
    val notes: List<NoteWithTasks> = emptyList(),
    val taskState: TaskState = TaskState()
)