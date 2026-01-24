package dev.amiah.ephemeral.viewmodel.userpreference

import dev.amiah.ephemeral.data.datastore.UserPreferences

data class UserPreferencesState(
    val userPreferences: UserPreferences = UserPreferences()
)
