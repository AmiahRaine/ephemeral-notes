package dev.amiah.ephemeral.viewmodel.longtermnote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.amiah.ephemeral.data.dao.ReminderDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.Instant

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
            ReminderEvent.CreateReminder -> TODO()
            is ReminderEvent.DeleteReminder -> TODO()
            is ReminderEvent.DeleteTask -> TODO()
            is ReminderEvent.ModifyReminderTextFieldValue -> TODO()
            is ReminderEvent.SaveReminder -> TODO()
            is ReminderEvent.SaveReminderText -> TODO()
            is ReminderEvent.SwitchCurrentTask -> TODO()
        }
    }

}