package dev.amiah.ephemeral.data.repository

import androidx.datastore.core.DataStore
import dev.amiah.ephemeral.data.datastore.ExpirationPolicy
import dev.amiah.ephemeral.data.datastore.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository @Inject constructor (private val dataStore: DataStore<UserPreferences>) {

    val userPreferences: Flow<UserPreferences> = dataStore.data

    suspend fun updateNotesExpirationPolicy(policy: ExpirationPolicy) = withContext(Dispatchers.IO) {
        dataStore.updateData { it.copy(notesExpirationPolicy = policy) }
    }

    suspend fun updateNotesHoldTime(holdTime: Int) = withContext(Dispatchers.IO) {
        dataStore.updateData { it.copy(notesHoldTime = holdTime) }
    }

    suspend fun getUserPreferencesOnce(): UserPreferences = withContext(Dispatchers.IO) {
        dataStore.data.first()
    }


}