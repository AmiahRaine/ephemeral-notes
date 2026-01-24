package dev.amiah.ephemeral.viewmodel.userpreference

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.amiah.ephemeral.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPreferencesViewModel @Inject constructor(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    private val _state = MutableStateFlow(UserPreferencesState())

    private val _prefs = userPreferencesRepository.userPreferences

    val state = combine(_state, _prefs) { state, prefs ->
        state.copy(
            userPreferences = prefs
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserPreferencesState())


    fun onUserPreferenceEvent(event: UserPreferenceEvent) {
        when (event) {
            is UserPreferenceEvent.SetNotesExpirationPolicy -> {
                viewModelScope.launch {
                    userPreferencesRepository.updateNotesExpirationPolicy(event.policy)
                }
            }
        }
    }
}