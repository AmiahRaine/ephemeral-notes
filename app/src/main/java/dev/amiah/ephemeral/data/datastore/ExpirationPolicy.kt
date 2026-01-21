package dev.amiah.ephemeral.data.datastore

import kotlinx.serialization.Serializable

@Serializable
enum class ExpirationPolicy {
    HOLD,
    TRANSFER_TO_END,
    SHUFFLE_DOWN
}