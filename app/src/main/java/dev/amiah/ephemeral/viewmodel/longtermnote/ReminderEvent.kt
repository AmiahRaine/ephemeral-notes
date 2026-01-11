package dev.amiah.ephemeral.viewmodel.longtermnote

import androidx.compose.ui.text.input.TextFieldValue
import dev.amiah.ephemeral.data.entity.Reminder
import dev.amiah.ephemeral.data.entity.Task
import java.time.LocalDateTime

sealed interface ReminderEvent {

    data class SaveReminder(val reminder: Reminder): ReminderEvent
    data class DeleteReminder(val reminder: Reminder): ReminderEvent

    data class SaveReminderText(val textFieldValue: TextFieldValue): ReminderEvent {
        constructor(text: String): this(TextFieldValue(text = text))
    }
    data class ModifyReminderTextFieldValue(val textValue: TextFieldValue): ReminderEvent

    data class CreateReminder(val dateTime: LocalDateTime): ReminderEvent
    data class SwitchCurrentTask(val task: Task?): ReminderEvent
    data class DeleteTask(val task: Task): ReminderEvent

    data class SetDateTimeModalVisibility(val show: Boolean): ReminderEvent

}