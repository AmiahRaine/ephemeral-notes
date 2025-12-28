package dev.amiah.ephemeral.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Note::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("parent_note_id"),
            onDelete = CASCADE
        )
    ]
)
data class Task (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "parent_note_id") val parentNoteId: Long,
    @ColumnInfo(name = "is_done") val isDone: Boolean = false,
    @ColumnInfo(name = "text") val text: String = ""
)