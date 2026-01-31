package dev.amiah.ephemeral.data.repository

import dev.amiah.ephemeral.data.dao.ReminderDao
import dev.amiah.ephemeral.data.entity.Reminder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemindersRepository @Inject constructor (private val reminderDao: ReminderDao) {

    suspend fun upsert(reminder: Reminder) = withContext(Dispatchers.IO) {
        reminderDao.upsert(reminder)
    }

    suspend fun delete(reminder: Reminder) = withContext(Dispatchers.IO) {
        reminderDao.delete(reminder)
    }

    suspend fun deleteInactiveReminders(cutoffTime: Instant) = withContext(Dispatchers.IO) {
        reminderDao.deleteInactiveReminders(cutoffTime)
    }

    // GETS

    fun getReminderById(id: Long): Flow<Reminder> {
        return reminderDao.getReminderById(id)
    }

    fun getActiveReminders(): Flow<List<Reminder>> {
        return reminderDao.getActiveRemindersByDate(Instant.now())
    }

    suspend fun getActiveRemindersOnce(): List<Reminder> {
        return reminderDao.getActiveRemindersByDateOnce(Instant.now())
    }

}