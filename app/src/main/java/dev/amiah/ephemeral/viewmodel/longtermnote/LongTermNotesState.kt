package dev.amiah.ephemeral.viewmodel.longtermnote

import dev.amiah.ephemeral.data.entity.LongTermNoteWithTasks
import dev.amiah.ephemeral.viewmodel.task.TaskState

data class LongTermNotesState (
    val ltNotes: List<LongTermNoteWithTasks> = emptyList(),
    val currentTask: TaskState = TaskState(),
)