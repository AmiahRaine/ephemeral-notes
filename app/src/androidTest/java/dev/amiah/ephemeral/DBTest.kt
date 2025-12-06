package dev.amiah.ephemeral

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.amiah.ephemeral.data.AppDatabase
import dev.amiah.ephemeral.data.dao.NoteDao
import dev.amiah.ephemeral.data.entity.Note
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.ZonedDateTime

@RunWith(AndroidJUnit4::class)
class DBTest {
    private lateinit var noteDao: NoteDao
    private lateinit var db: AppDatabase

    @Before
    fun createDB() {
        val context = ApplicationProvider.getApplicationContext<Context>();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build();
        noteDao = db.noteDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun testData() = runBlocking  {
        // Simple output test

        noteDao.insert(Note(id = 0, day = ZonedDateTime.now()))

        println(noteDao.getNoteById(0))


    }

}