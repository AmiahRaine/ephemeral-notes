package dev.amiah.ephemeral.viewmodel.note

import androidx.compose.ui.text.input.TextFieldValue
import dev.amiah.ephemeral.data.entity.Note
import dev.amiah.ephemeral.data.entity.Task

sealed interface NotesEvent {
    object SaveNote: NotesEvent

    data class SaveTaskText(val task: Task, val textFieldValue: TextFieldValue): NotesEvent

    data class SaveTaskIsDone(val task: Task, val isDone: Boolean): NotesEvent

    data class CreateTask(val parentId: Long): NotesEvent

    data class DeleteNote(val note: Note): NotesEvent
    data class DeleteTask(val task: Task): NotesEvent

    data class SwitchCurrentTask(val task: Task): NotesEvent

    data class ModifyTextValue(val textValue: TextFieldValue): NotesEvent


}