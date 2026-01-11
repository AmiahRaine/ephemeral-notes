package dev.amiah.ephemeral.viewmodel.longtermnote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.amiah.ephemeral.data.dao.ReminderDao
import dev.amiah.ephemeral.data.entity.Reminder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class RemindersViewModel(private val reminderDao: ReminderDao) : ViewModel() {

    // Default ReminderState.
    private val _state = MutableStateFlow(RemindersState())

    private val _reminders = reminderDao.getActiveRemindersByDate(Instant.now())

    val state = combine(_state, _reminders) { state, reminders ->
        state.copy(
            reminders = reminders
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RemindersState())

    // Used to debounce saving the text.
    private var _saveTextJob: Job? = null;

    fun onReminderEvent(event: ReminderEvent) {
        when (event) {
            is ReminderEvent.CreateReminder -> {
                viewModelScope.launch(Dispatchers.IO) {
                    reminderDao.upsert(Reminder(time = ZonedDateTime.of(event.dateTime, ZoneId.systemDefault()).toInstant()))
                }
            }
            is ReminderEvent.DeleteReminder -> TODO()
            is ReminderEvent.DeleteTask -> TODO()
            is ReminderEvent.ModifyReminderTextFieldValue -> TODO()
            is ReminderEvent.SaveReminder -> {
                viewModelScope.launch(Dispatchers.IO) {
                    reminderDao.upsert(event.reminder)
                }
            }
            is ReminderEvent.SaveReminderText -> TODO()
            is ReminderEvent.SwitchCurrentTask -> TODO()
            is ReminderEvent.SetDateTimeModalVisibility -> {
                _state.update { it.copy(showDateTimePicker = event.show) }
            }
        }
    }

}