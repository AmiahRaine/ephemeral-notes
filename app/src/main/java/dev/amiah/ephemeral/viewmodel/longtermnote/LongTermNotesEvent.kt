package dev.amiah.ephemeral.viewmodel.longtermnote

import androidx.compose.ui.text.input.TextFieldValue
import dev.amiah.ephemeral.data.entity.LongTermNote
import dev.amiah.ephemeral.data.entity.Task

sealed interface LongTermNotesEvent {

    data class SaveNote(val ltNote: LongTermNote): LongTermNotesEvent
    data class DeleteNote(val ltNote: LongTermNote): LongTermNotesEvent

    data class SaveTaskIsDone(val task: Task, val isDone: Boolean): LongTermNotesEvent
    data class SaveTaskText(val task: Task, val textFieldValue: TextFieldValue): LongTermNotesEvent
    data class ModifyTaskTextFieldValue(val textValue: TextFieldValue): LongTermNotesEvent

    data class CreateTask(val parentId: Long): LongTermNotesEvent
    data class SwitchCurrentTask(val task: Task?): LongTermNotesEvent
    data class DeleteTask(val task: Task): LongTermNotesEvent

}