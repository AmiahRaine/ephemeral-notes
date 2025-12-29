package dev.amiah.ephemeral

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import dev.amiah.ephemeral.data.AppDatabase
import dev.amiah.ephemeral.ui.screen.NoteSlider
import dev.amiah.ephemeral.ui.theme.EphemeralTheme
import dev.amiah.ephemeral.viewmodel.note.NotesViewModel

class MainActivity : ComponentActivity() {

    lateinit var db: AppDatabase;

    private val viewModel by viewModels<NotesViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NotesViewModel(db.noteDao(), db.taskDao()) as T
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

        viewModel.manageActiveNotes()
        viewModel.manageInactiveNotes()

        setContent {

            val state by viewModel.state.collectAsState(null)

            EphemeralTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {

                        // This needs its own state
                        Box(modifier = Modifier.weight(0.25f)) {
                            //NoteSlider(state, viewModel::onNoteEvent)
                        }
                        // This is the regular one that the state was made for
                        Box(modifier = Modifier.weight(1f)){NoteSlider(state, viewModel::onNoteEvent)}
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