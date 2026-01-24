package dev.amiah.ephemeral.viewmodel.userpreference

import dev.amiah.ephemeral.data.datastore.ExpirationPolicy

interface UserPreferenceEvent {
    data class SetNotesExpirationPolicy(val policy: ExpirationPolicy): UserPreferenceEvent
}