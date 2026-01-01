package dev.amiah.ephemeral.viewmodel.longtermnote

import dev.amiah.ephemeral.data.dao.NoteDao
import dev.amiah.ephemeral.data.dao.TaskDao
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class LongTermNotesViewModel(private val noteDao: NoteDao, private val taskDao: TaskDao) {

}