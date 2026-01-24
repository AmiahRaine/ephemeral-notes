package dev.amiah.ephemeral.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.amiah.ephemeral.ui.element.ExpirationPolicySelectionMenu
import dev.amiah.ephemeral.viewmodel.userpreference.UserPreferenceEvent
import dev.amiah.ephemeral.viewmodel.userpreference.UserPreferencesState

@Composable
fun SettingsScreen(userPreferenceState: UserPreferencesState?, onEvent: (UserPreferenceEvent) -> Unit) {
    // Expired Notes Policy
    // Select days to save expired notes
    val expanded = remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("Notes Expiration Policy")
        ExpirationPolicySelectionMenu(userPreferenceState, onEvent, modifier = Modifier.fillMaxWidth() )
    }
}