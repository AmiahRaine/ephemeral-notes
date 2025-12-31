package dev.amiah.ephemeral.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.Instant

@Entity
data class LongTermNote (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "day") val time: Instant
)

data class LongTermNoteWithTasks (
    @Embedded val longTermNote: LongTermNote,
    @Relation(
        parentColumn = "id",
        entityColumn = "parent_note_id"
    )
    val tasks: List<Task>
)