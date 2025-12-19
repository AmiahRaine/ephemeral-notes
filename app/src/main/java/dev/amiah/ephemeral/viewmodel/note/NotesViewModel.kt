package dev.amiah.ephemeral.viewmodel.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.amiah.ephemeral.data.dao.NoteDao
import dev.amiah.ephemeral.data.dao.TaskDao
import dev.amiah.ephemeral.data.entity.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModel(private val noteDao: NoteDao, private val taskDao: TaskDao) : ViewModel() {

    // Default NotesState.
    private val _state = MutableStateFlow(NotesState())

    private val _notes = noteDao.getActiveNotesWithTasksByDate(Instant.now())

    val state = combine(_state, _notes) { state, notes ->
        state.copy(
            notes = notes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NotesState())


    fun onNoteEvent(event: NotesEvent) {
        when (event) {
            NotesEvent.SaveNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    noteDao.upsert()
                }
            }

            is NotesEvent.DeleteNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    noteDao.delete(event.note)
                }
            }


            is NotesEvent.DeleteTask -> TODO()

            is NotesEvent.SaveTask -> {
                viewModelScope.launch(Dispatchers.IO) {
                    taskDao.upsert(event.task)
                }
            }
        }

    }

    fun manageActiveNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            val minActiveDays = 7 // TODO: Replace with user preference
            val currentTime = Instant.now() // Save time so it stays consistent across usages
            val notes = noteDao.getActiveNotesByDateOnce(currentTime)
            val remainingDays = maxOf(0, minActiveDays - notes.size)
            // This is the time of the last active note. Use current time if no notes currently exist.
            val startTime: Instant = if (notes.isEmpty()) currentTime else notes.last().time

            // Create list of notes
            val notesList = (1..remainingDays).map { daysToAdd ->
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
    }

    // Delete notes that have expired.
    fun manageInactiveNotes() {
        val daysToRetain = 7
        val cutoffTime = Instant.now()
            .atZone(ZoneId.systemDefault()) // convert to user time
            .toLocalDate() // remove time info, only date is needed
            .minusDays(daysToRetain.toLong()) // subtract number of days to keep notes
            .atStartOfDay(ZoneId.systemDefault()) // convert to zoned date time
            .toInstant() // put back to instant

        viewModelScope.launch(Dispatchers.IO) {
            noteDao.deleteInactiveNotes(cutoffTime)
        }
    }


}