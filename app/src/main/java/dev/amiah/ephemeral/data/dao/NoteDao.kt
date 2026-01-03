package dev.amiah.ephemeral.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.amiah.ephemeral.data.entity.Note
import dev.amiah.ephemeral.data.entity.NoteWithTasks
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface NoteDao {

    @Upsert
    suspend fun upsert(vararg notes: Note)

    @Delete
    suspend fun delete(vararg notes: Note)

    @Query("SELECT * FROM note WHERE time >= :now ")
    suspend fun getActiveNotesByDateOnce(now: Instant): List<Note>

    @Query("DELETE FROM note where time < :cutoffTime")
    suspend fun deleteInactiveNotes(cutoffTime: Instant)

    @Query("SELECT * FROM note WHERE time >= :now ")
    fun getActiveNotesByDate(now: Instant): Flow<List<Note>>

    @Transaction
    @Query("SELECT * FROM note WHERE time >= :instant ")
    fun getActiveNotesWithTasksByDate(instant: Instant): Flow<List<NoteWithTasks>>

    @Query("SELECT * FROM note WHERE time < :now ")
    fun getInactiveNotesByDate(now: Instant): Flow<List<Note>>

    @Transaction
    @Query("SELECT * FROM note WHERE time < :now ")
    fun getInactiveNotesWithTasksByDate(now: Instant): Flow<List<NoteWithTasks>>

    @Query("SELECT * FROM note WHERE id = :id")
    fun getNoteById(id: Long): Flow<Note>

    @Transaction
    @Query("SELECT * FROM note WHERE id = :id")
    fun getNoteWithTasksById(id: Long): Flow<List<NoteWithTasks>>

    @Query("SELECT * FROM note")
    fun getAllNotes(): Flow<List<Note>>

    @Transaction
    @Query("SELECT * FROM note")
    fun getAllNotesWithTasks(): Flow<List<NoteWithTasks>>

}