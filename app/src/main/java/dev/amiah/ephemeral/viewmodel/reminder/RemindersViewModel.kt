package dev.amiah.ephemeral.viewmodel.reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.amiah.ephemeral.data.dao.ReminderDao
import dev.amiah.ephemeral.data.entity.Reminder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RemindersViewModel @Inject constructor (private val reminderDao: ReminderDao) : ViewModel() {

    // Default ReminderState.
    private val _state = MutableStateFlow(RemindersState())

    private val _reminders = reminderDao.getActiveRemindersByDate(Instant.now())

    val state = combine(_state, _reminders) { state, reminders ->
        state.copy(
            reminders = reminders
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RemindersState())

    // Used to debounce saving.
    private var _debounceSaveJob: Job? = null;

    fun onReminderEvent(event: ReminderEvent) {
        when (event) {

            // Create, Delete, Save Reminders

            is ReminderEvent.CreateReminder -> {
                viewModelScope.launch(Dispatchers.IO) {
                    reminderDao.upsert(Reminder(time = ZonedDateTime.of(event.dateTime, ZoneId.systemDefault()).toInstant()))
                }
            }

            is ReminderEvent.DeleteReminder -> {
                viewModelScope.launch(Dispatchers.IO) {
                    reminderDao.delete(event.reminder)
                }
            }

            is ReminderEvent.SaveReminder -> {
                viewModelScope.launch(Dispatchers.IO) {
                    reminderDao.upsert(event.reminder)
                }
            }

            is ReminderEvent.SaveReminderWithDebounce -> {
                // Saving certain things such as text needs to be debounced to prevent unintended behavior
                // Start by canceling any previous job
                _debounceSaveJob?.cancel()

                _debounceSaveJob = viewModelScope.launch(Dispatchers.IO) {
                    // Give time for job to be canceled
                    delay(120)
                    // If no new job comes in, do the save
                    reminderDao.upsert(event.reminder)
                }
            }

            // Alter text, modal state, etc

            is ReminderEvent.SetReminderTextFieldValue -> {
                _state.update { it.copy(currentReminderText = event.textValue) }
            }

            is ReminderEvent.SetDateTimeModalVisibility -> {
                _state.update { it.copy(showDateTimePicker = event.show) }
            }
        }
    }

}