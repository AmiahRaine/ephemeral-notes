package dev.amiah.ephemeral.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.amiah.ephemeral.data.dao.NoteDao
import dev.amiah.ephemeral.data.dao.TaskDao
import dev.amiah.ephemeral.data.entity.LongTermNote
import dev.amiah.ephemeral.data.entity.Note
import dev.amiah.ephemeral.data.entity.Task

@Database(entities = [Note::class, Task::class, LongTermNote::class], version = 4)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    abstract fun taskDao(): TaskDao
}