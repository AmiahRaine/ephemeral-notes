package dev.amiah.ephemeral

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import dev.amiah.ephemeral.data.AppDatabase
import dev.amiah.ephemeral.ui.element.ActionBar
import dev.amiah.ephemeral.ui.element.NoteSlider
import dev.amiah.ephemeral.ui.element.ReminderSlider
import dev.amiah.ephemeral.ui.theme.EphemeralTheme
import dev.amiah.ephemeral.viewmodel.longtermnote.RemindersViewModel
import dev.amiah.ephemeral.viewmodel.note.NoteEvent
import dev.amiah.ephemeral.viewmodel.note.NotesViewModel

class MainActivity : ComponentActivity() {

    lateinit var db: AppDatabase;

    private val notesViewModel by viewModels<NotesViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NotesViewModel(db.noteDao(), db.taskDao()) as T
                }
            }
        }
    )

    private val remindersViewModel by viewModels<RemindersViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RemindersViewModel(db.reminderDao()) as T
                }
            }
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // TODO: Remove destructive migration. Is only temporary while testing
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "ephemeral.db"
        ).fallbackToDestructiveMigration().build();

        notesViewModel.manageActiveNotes()
        notesViewModel.manageInactiveNotes()

        setContent {

            val notesState by notesViewModel.state.collectAsState(null)
            val remindersState by remindersViewModel.state.collectAsState(null)
            val focusManager = LocalFocusManager.current

            EphemeralTheme {
                Scaffold(modifier = Modifier.fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures {
                            // Make text fields loose focus when clicking off of them
                            focusManager.clearFocus();
                            // Set current task to null, since no text fields are currently selected
                            notesViewModel.onNoteEvent(NoteEvent.SwitchCurrentTask(null))
                        }
                    }
                ) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {

                        Box() {
                            ActionBar()
                        }

                        Box(modifier = Modifier.weight(0.25f)) {
                            ReminderSlider(remindersState, remindersViewModel::onReminderEvent)
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            NoteSlider(notesState, notesViewModel::onNoteEvent)
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EphemeralTheme {
        Greeting("Android")
    }
}