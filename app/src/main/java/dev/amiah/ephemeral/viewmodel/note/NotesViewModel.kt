package dev.amiah.ephemeral.viewmodel.note

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.amiah.ephemeral.data.dao.NoteDao
import dev.amiah.ephemeral.data.dao.TaskDao
import dev.amiah.ephemeral.data.entity.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class NotesViewModel @Inject constructor (private val noteDao: NoteDao, private val taskDao: TaskDao) : ViewModel() {

    // Default NotesState.
    private val _state = MutableStateFlow(NotesState())

    // Minus one day since Instant includes time info which we don't care about in this case.
    private val _notes = noteDao.getActiveNotesWithTasksByDate(Instant.now().minus(1, ChronoUnit.DAYS))

    val state = combine(_state, _notes) { state, notes ->
        state.copy(
            notes = notes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NotesState())

    // Used to debounce saving the text.
    private var _saveTextJob: Job? = null;

    fun onNoteEvent(event: NoteEvent) {
        when (event) {

            // NOTE OPERATIONS

            is NoteEvent.SaveNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    noteDao.upsert(event.note)
                }
            }

            is NoteEvent.DeleteNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    noteDao.delete(event.note)
                }
            }

            // TASK OPERATIONS

            is NoteEvent.CreateTask -> {
                // Do not proceed if invalid parent id
                if (event.parentId < 1) return

                // Do not create a new task if the current task is still new and empty
                val currentState = _state.value.copy()
                if (currentState.currentTaskIsNew && currentState.currentTaskText.text.isEmpty()) return

                viewModelScope.launch {
                    val newTask = Task(parentNoteId = event.parentId)
                    val newId = taskDao.insert(newTask)
                    // Switches selected task to the newly inserted. Marks it new to prevent deletion when empty.
                    // Is new flag gets unset when switching to a different task.
                    _state.update {
                        it.copy(
                            currentTask = newTask.copy(id = newId),
                            currentTaskIsNew = true,
                            currentTaskText = TextFieldValue()
                        )
                    }
                }
            }

            is NoteEvent.SaveTaskIsDone -> {
                // Reject task if invalid id.
                if (event.task.parentNoteId < 1) return

                // Change isDone and save change
                viewModelScope.launch(Dispatchers.IO) {
                    taskDao.upsert(event.task.copy(isDone = event.isDone))
                }
            }

            is NoteEvent.SaveTaskText -> {
                // Saving the text needs to be debounced to prevent unintended behavior
                // Start by canceling any previous job
                _saveTextJob?.cancel()

                _saveTextJob = viewModelScope.launch(Dispatchers.IO) {
                    // Give time for job to be canceled
                    delay(120)
                    // If no new job comes in, save the text
                    taskDao.upsert(event.task)
                }
            }

            is NoteEvent.ModifyTaskTextFieldValue -> {
                _state.update { it.copy(currentTaskText = event.textValue) }
            }


            is NoteEvent.SwitchCurrentTask -> {
                // Fixes bug of empty new text fields persisting after switch
                deleteFormerTaskIfEmpty()

                // The current task is the one that is selected. Switching should mark the task as being not new.
                // Sets text field value to place cursor at end.
                _state.update {
                    it.copy(
                        currentTask = event.task,
                        currentTaskIsNew = false,
                        currentTaskText = TextFieldValue(event.task?.text ?: "", TextRange(event.task?.text?.length ?: 0))
                    )
                }
            }

            is NoteEvent.DeleteTask -> {
                viewModelScope.launch(Dispatchers.IO) {
                    taskDao.delete(event.task)
                }
            }
        }

    }

    /**
     * Checks if the former task is empty, and deletes it if true. Uses a copy
     * of _state to check.
     */
    private fun deleteFormerTaskIfEmpty() {
        val formerState = _state.value.copy()
        if (formerState.currentTaskText.text.isEmpty()
            && formerState.currentTask != null) {
            viewModelScope.launch(Dispatchers.IO) {
                taskDao.delete(formerState.currentTask)
            }
        }
    }


}