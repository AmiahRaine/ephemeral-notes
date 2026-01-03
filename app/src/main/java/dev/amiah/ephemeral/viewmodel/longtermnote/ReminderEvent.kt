package dev.amiah.ephemeral.viewmodel.longtermnote

import androidx.compose.ui.text.input.TextFieldValue
import dev.amiah.ephemeral.data.entity.Reminder
import dev.amiah.ephemeral.data.entity.Task

sealed interface ReminderEvent {

    data class SaveReminder(val reminder: Reminder): ReminderEvent
    data class DeleteReminder(val reminder: Reminder): ReminderEvent

    data class SaveReminderText(val textFieldValue: TextFieldValue): ReminderEvent {
        constructor(text: String): this(TextFieldValue(text = text))
    }
    data class ModifyReminderTextFieldValue(val textValue: TextFieldValue): ReminderEvent

    object CreateReminder: ReminderEvent
    data class SwitchCurrentTask(val task: Task?): ReminderEvent
    data class DeleteTask(val task: Task): ReminderEvent

}