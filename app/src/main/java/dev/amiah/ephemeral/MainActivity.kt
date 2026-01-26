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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import dev.amiah.ephemeral.domain.worker.DatabaseManagementWorker
import dev.amiah.ephemeral.ui.element.ActionBar
import dev.amiah.ephemeral.ui.element.NoteSlider
import dev.amiah.ephemeral.ui.element.ReminderSlider
import dev.amiah.ephemeral.ui.screen.SettingsScreen
import dev.amiah.ephemeral.ui.theme.EphemeralTheme
import dev.amiah.ephemeral.viewmodel.longtermnote.RemindersState
import dev.amiah.ephemeral.viewmodel.longtermnote.RemindersViewModel
import dev.amiah.ephemeral.viewmodel.note.NoteEvent
import dev.amiah.ephemeral.viewmodel.note.NotesState
import dev.amiah.ephemeral.viewmodel.note.NotesViewModel
import dev.amiah.ephemeral.viewmodel.userpreference.UserPreferencesViewModel
import kotlinx.serialization.Serializable


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Serializable
    object MainScreen
    @Serializable
    object ViewReminders
    @Serializable
    object ViewTasks
    @Serializable
    object Settings

    private val notesViewModel by viewModels<NotesViewModel>()

    private val remindersViewModel by viewModels<RemindersViewModel>()

    private val userPreferencesViewModel by viewModels<UserPreferencesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val databaseManagementWorkRequest = OneTimeWorkRequestBuilder<DatabaseManagementWorker>()
            .build()

        WorkManager.getInstance(applicationContext).beginUniqueWork(
            uniqueWorkName = "ephemeral.manage.data",
            existingWorkPolicy = ExistingWorkPolicy.KEEP,
            request = databaseManagementWorkRequest
        )

        setContent {

            val navController = rememberNavController()
            val notesState by notesViewModel.state.collectAsState()
            val remindersState by remindersViewModel.state.collectAsState()
            val userPreferencesState by userPreferencesViewModel.state.collectAsState()
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

                        NavHost(navController, startDestination = MainScreen) {

                            composable<MainScreen> {
                                MainScreen(
                                    remindersState, remindersViewModel,
                                    notesState, notesViewModel, navController
                                )
                            }

                            composable<ViewReminders> {  }

                            composable<ViewTasks> {  }

                            composable<Settings> {
                                SettingsScreen(
                                userPreferencesState,
                                userPreferencesViewModel::onUserPreferenceEvent
                                )
                            }

                        }

                    }
                }
            }
        }
    }

}

/**
 * Primary screen of the app
 */
@Composable
fun MainScreen(
    remindersState: RemindersState?, remindersViewModel: RemindersViewModel,
    notesState: NotesState?, notesViewModel: NotesViewModel,
    navController: NavHostController
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box {
            ActionBar(navController)
        }

        Box(modifier = Modifier.weight(0.25f)) {
            ReminderSlider(remindersState, remindersViewModel::onReminderEvent)
        }

        Box(modifier = Modifier.weight(1f)) {
            NoteSlider(notesState, notesViewModel::onNoteEvent)
        }
    }
}