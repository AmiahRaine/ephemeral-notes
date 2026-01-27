package dev.amiah.ephemeral.viewmodel.reminder

import androidx.compose.ui.text.input.TextFieldValue
import dev.amiah.ephemeral.data.entity.Reminder

data class RemindersState (
    val reminders: List<Reminder> = emptyList(),
    val currentReminderText: TextFieldValue = TextFieldValue(),
    val currentReminderIsNew: Boolean = false,
    val showDateTimePicker: Boolean = false
)