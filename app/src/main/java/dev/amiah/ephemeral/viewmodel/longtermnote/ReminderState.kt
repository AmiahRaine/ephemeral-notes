package dev.amiah.ephemeral.viewmodel.longtermnote

import androidx.compose.ui.text.input.TextFieldValue
import dev.amiah.ephemeral.data.entity.Reminder

data class ReminderState (
    val reminders: List<Reminder> = emptyList(),
    val currentReminder: Reminder? = null,
    val currentReminderText: TextFieldValue = TextFieldValue(),
    val currentReminderIsNew: Boolean = false
)