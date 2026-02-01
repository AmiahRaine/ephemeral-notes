package dev.amiah.ephemeral.domain.manager

import dev.amiah.ephemeral.data.dao.NoteDao
import dev.amiah.ephemeral.data.dao.TaskDao
import dev.amiah.ephemeral.data.entity.Note
import dev.amiah.ephemeral.data.repository.RemindersRepository
import dev.amiah.ephemeral.data.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseManager @Inject constructor (
    private val userPreferencesRepository: UserPreferencesRepository,
    private val noteDao: NoteDao,
    private val taskDao: TaskDao,
    private val remindersRepository: RemindersRepository
) {

    suspend fun manageActiveNotes() = withContext(Dispatchers.IO) {
        // Save time so it stays consistent across usages
        val currentTime = Instant.now()
        val notes = noteDao.getActiveNotesByDateOnce(currentTime)
        val userPref = userPreferencesRepository.getUserPreferencesOnce()

        // Calculate the number of new days of notes to create
        val minActiveDays = userPref.minActiveDays
        val remainingDays = maxOf(0, minActiveDays - notes.size - 1)

        // This is the time of the last active note. Use current time if no notes currently exist.
        val startTime: Instant = if (notes.isEmpty()) currentTime else notes.last().time
        // If no notes exist, we should start from today, otherwise start from tomorrow
        val startValue = if (notes.isEmpty()) 0 else 1

        // Create list of notes
        val notesList = (startValue..remainingDays).map { daysToAdd ->
            Note(
                time = startTime
                    .atZone(ZoneId.systemDefault()) // convert to user time
                    .toLocalDate() // remove time info, only date is needed
                    .plusDays(daysToAdd.toLong()) // increase time to next days
                    .atStartOfDay(ZoneId.systemDefault()) // convert to zoned date time
                    .toInstant() // put back to instant
            )
        }

        // Add notes to db
        if (notesList.isNotEmpty()) {
            noteDao.upsert(*notesList.toTypedArray())
        }
    }

    // Delete notes that have expired.
    suspend fun manageInactiveNotes() = withContext(Dispatchers.IO) {
        val userPref = userPreferencesRepository.getUserPreferencesOnce()

        val daysToRetain = userPref.notesHoldTime
        // Set cutoffTime to the current time but at start of day
        val cutoffTime = Instant.now()
            .atZone(ZoneId.systemDefault()) // convert to user time
            .toLocalDate() // remove time info, only date is needed
            .minusDays(daysToRetain.toLong()) // subtract number of days to keep notes
            .atStartOfDay(ZoneId.systemDefault()) // convert to zoned date time
            .toInstant() // put back to instant

        noteDao.deleteInactiveNotes(cutoffTime)
    }

}