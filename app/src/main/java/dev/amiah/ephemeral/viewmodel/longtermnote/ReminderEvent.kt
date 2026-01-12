package dev.amiah.ephemeral.viewmodel.longtermnote

import androidx.compose.ui.text.input.TextFieldValue
import dev.amiah.ephemeral.data.entity.Reminder
import java.time.LocalDateTime

sealed interface ReminderEvent {

    data class CreateReminder(val dateTime: LocalDateTime): ReminderEvent
    data class SaveReminder(val reminder: Reminder): ReminderEvent
    data class SaveReminderWithDebounce(val reminder: Reminder): ReminderEvent
    data class DeleteReminder(val reminder: Reminder): ReminderEvent

    data class ModifyReminderTextFieldValue(val textValue: TextFieldValue): ReminderEvent

    data class SetDateTimeModalVisibility(val show: Boolean): ReminderEvent

}