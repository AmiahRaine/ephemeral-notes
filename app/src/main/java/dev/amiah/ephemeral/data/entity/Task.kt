package dev.amiah.ephemeral.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task (
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "parent_note_id") val parentNoteId: Long,
    @ColumnInfo(name = "is_done") val isDone: Boolean?,
    @ColumnInfo(name = "text") val text: String?
)