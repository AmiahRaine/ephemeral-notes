package dev.amiah.ephemeral.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.ZonedDateTime

@Entity
data class Note (
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "day") val day: ZonedDateTime
)

data class NoteWithTasks (
    @Embedded val note: Note,
    @Relation(
        parentColumn = "id",
        entityColumn = "parent_note_id"
    )
    val tasks: List<Task>
)