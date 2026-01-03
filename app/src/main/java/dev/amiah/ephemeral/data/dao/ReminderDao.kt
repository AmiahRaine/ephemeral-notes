package dev.amiah.ephemeral.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import dev.amiah.ephemeral.data.entity.Reminder
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface ReminderDao {

    @Upsert
    suspend fun upsert(vararg reminder: Reminder)

    @Delete
    suspend fun delete(vararg reminder: Reminder)

    @Query("SELECT * FROM reminder WHERE time >= :now ")
    suspend fun getActiveRemindersByDateOnce(now: Instant): List<Reminder>

    @Query("DELETE FROM reminder where time < :cutoffTime")
    suspend fun deleteInactiveReminders(cutoffTime: Instant)

    @Query("SELECT * FROM reminder WHERE id = :id")
    fun getReminderById(id: Long): Flow<Reminder>

}