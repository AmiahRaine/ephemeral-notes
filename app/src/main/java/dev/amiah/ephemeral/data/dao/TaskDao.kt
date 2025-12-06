package dev.amiah.ephemeral.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.amiah.ephemeral.data.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg tasks: Task)

    @Update
    suspend fun update(vararg tasks: Task)

    @Delete
    suspend fun delete(vararg tasks: Task)

}