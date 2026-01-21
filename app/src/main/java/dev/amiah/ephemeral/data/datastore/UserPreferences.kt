package dev.amiah.ephemeral.data.datastore

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val notesExpirationPolicy: ExpirationPolicy = ExpirationPolicy.HOLD
)
