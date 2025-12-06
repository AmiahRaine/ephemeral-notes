package dev.amiah.ephemeral.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.amiah.ephemeral.data.entity.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg notes: Note)

    @Update
    suspend fun update(vararg notes: Note)

    @Delete
    suspend fun delete(vararg notes: Note)

    // Where day is today or later
    //TODO
    @Query("SELECT * FROM note WHERE day")
    fun getActiveNotesByDate(): Flow<Note>

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteById(id: Int): Note

}