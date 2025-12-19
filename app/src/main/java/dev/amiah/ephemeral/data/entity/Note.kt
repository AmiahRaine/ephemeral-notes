package dev.amiah.ephemeral.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.Instant

@Entity
data class Note (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "time") val time: Instant
)

data class NoteWithTasks (
    @Embedded val note: Note,
    @Relation(
        parentColumn = "id",
        entityColumn = "parent_note_id"
    )
    val tasks: List<Task>
)