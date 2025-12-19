package dev.amiah.ephemeral.viewmodel.note

import dev.amiah.ephemeral.data.entity.Note
import dev.amiah.ephemeral.data.entity.Task
sealed interface NotesEvent {
    object SaveNote: NotesEvent
    data class SaveTask(val task: Task): NotesEvent

    data class DeleteNote(val note: Note): NotesEvent
    data class DeleteTask(val task: Task): NotesEvent



}