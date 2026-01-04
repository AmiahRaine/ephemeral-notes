package dev.amiah.ephemeral.viewmodel.note

import androidx.compose.ui.text.input.TextFieldValue
import dev.amiah.ephemeral.data.entity.Note
import dev.amiah.ephemeral.data.entity.Task

sealed interface NoteEvent {
    data class SaveNote(val note: Note): NoteEvent
    data class DeleteNote(val note: Note): NoteEvent

    data class SaveTaskIsDone(val task: Task, val isDone: Boolean): NoteEvent
    data class SaveTaskText(val task: Task, val textFieldValue: TextFieldValue): NoteEvent {
        constructor(task: Task, text: String): this(task, TextFieldValue(text = text))
    }
    data class ModifyTaskTextFieldValue(val textValue: TextFieldValue): NoteEvent

    data class CreateTask(val parentId: Long): NoteEvent
    data class SwitchCurrentTask(val task: Task?): NoteEvent
    data class DeleteTask(val task: Task): NoteEvent

}