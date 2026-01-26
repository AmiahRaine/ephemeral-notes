package dev.amiah.ephemeral.data.datastore

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val notesExpirationPolicy: ExpirationPolicy = ExpirationPolicy.HOLD,
    val notesHoldTime: Int = 7, // Days
    val minActiveDays: Int = 7 // Number of days to have notes for
)
