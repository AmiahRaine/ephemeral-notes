package dev.amiah.ephemeral.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import dev.amiah.ephemeral.data.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Upsert
    suspend fun upsert(vararg tasks: Task)

    @Delete
    suspend fun delete(vararg tasks: Task)

    @Query("SELECT * FROM task WHERE id = :id")
    fun get(id: Int): Flow<Task>

    @Query("SELECT * FROM task WHERE parent_note_id = :noteId")
    fun getAllById(noteId: Int): Flow<List<Task>>

}