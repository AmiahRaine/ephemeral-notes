package dev.amiah.ephemeral.data.datastore

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val notesExpirationPolicy: ExpirationPolicy = ExpirationPolicy.HOLD,
    val notesHoldTime: Int = 7 // Days
)
