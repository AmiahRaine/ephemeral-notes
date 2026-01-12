package dev.amiah.ephemeral.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import dev.amiah.ephemeral.data.entity.Reminder
import dev.amiah.ephemeral.util.Format
import dev.amiah.ephemeral.viewmodel.longtermnote.ReminderEvent
import dev.amiah.ephemeral.viewmodel.longtermnote.RemindersState
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue

@Composable
fun ReminderSlider(remindersState: RemindersState?, onEvent: (ReminderEvent) -> Unit) {

    val pagerState = rememberPagerState(pageCount = { (remindersState?.reminders?.size?.plus(1)) ?: 1 })

    HorizontalPager(pagerState) { pageNumber ->
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .graphicsLayer {
                    // Offset of current page from scroll position
                    val pageOffset = ((pagerState.currentPage - pageNumber)
                            + pagerState.currentPageOffsetFraction).absoluteValue
                    // Interpolate alpha based on page offset
                    alpha = lerp(0.5f, 1f, 1f - pageOffset.coerceIn(0f, 1f))

                    // Interpolate size based on page offset
                    scaleX = lerp(1f, 0.9f, pageOffset.coerceIn(0f, 1f))
                    scaleY = scaleX
                }
        ) {
            // If not last page display reminder (last page is for add new button)
            if (pageNumber + 1 != pagerState.pageCount) {
                Text(text = "${
                    remindersState?.reminders?.get(pageNumber)?.time?.atZone(
                        ZoneId.systemDefault())?.format(DateTimeFormatter.ofPattern("MMMM d, yyyy, h:mm a"))
                }.", modifier = Modifier.clickable {onEvent(ReminderEvent.SetDateTimeModalVisibility(true))} )

                ReminderTextField(remindersState?.reminders?.get(pageNumber), remindersState, onEvent)
            }
            // Otherwise show add new button
            else {
                Button( // ADD REMINDER BUTTON
                    modifier = Modifier.fillMaxSize(),
                    onClick = {onEvent(ReminderEvent.SetDateTimeModalVisibility(true))}
                ) {
                    Text("ADD REMINDER")
                }
            }

            // Show modal
            if (remindersState?.showDateTimePicker == true) {
                if (pageNumber + 1 != pagerState.pageCount) {
                    PickDateTimeModal(remindersState.reminders[pageNumber], onEvent)
                }
                else {
                    PickDateTimeModal(Reminder(), onEvent)
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickDateTimeModal(reminder: Reminder, onEvent: (ReminderEvent) -> Unit) {

    // For whether modal shows time picker or date picker
    val pickerMode = remember { mutableStateOf(false) }

    // Set initial time to the time saved in the reminder. If unset, set the initial time to
    // the current time plus one hour at zero minutes
    val localDateTime: LocalDateTime =
        // Redundant null check in case I ever change time field to Instant? which I probably won't
        // but my brain is making me do it anyways
        if (reminder.time != Instant.MIN && reminder.time != null) {
            reminder.time.atZone(ZoneId.systemDefault()).toLocalDateTime()
        }
        else {
            LocalDateTime.now().plusHours(1).withMinute(0)
        }

    val datePickerState = rememberDatePickerState(
        initialSelectedDate = localDateTime.toLocalDate(),
        yearRange = (LocalDate.now().year..LocalDate.now().plusYears(3).year)
    )
    val timePickerState = rememberTimePickerState(
        localDateTime.toLocalTime().hour,
        localDateTime.toLocalTime().minute
    )

    Dialog(
        onDismissRequest = {onEvent(ReminderEvent.SetDateTimeModalVisibility(false))},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(modifier = Modifier.padding(vertical = 64.dp, horizontal = 16.dp)) {
            Column() {
                // Picker Mode Select Buttons
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(modifier = Modifier.weight(0.5f), onClick = {pickerMode.value = false}) { Text("TIME") }
                    Button(modifier = Modifier.weight(0.5f), onClick = {pickerMode.value = true}) { Text("DATE") }
                }
                // Date or Time Picker depending on mode
                if (pickerMode.value) {
                        DatePicker(datePickerState, modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                        )
                }
                else {
                    TimePicker(timePickerState, modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 46.dp, bottom = 10.dp)
                    )
                }

                // Show selected date at bottom of modal + done button
                Row(modifier = Modifier.padding(6.dp).fillMaxWidth()) {
                    Column(modifier = Modifier.weight(0.8f)) {
                        Text("Selected Time")
                        Text("${Format.localDateToHumanString(datePickerState.getSelectedDate())}, ${Format.hourMinToHumanString(timePickerState.hour, timePickerState.minute, timePickerState.is24hour)}")
                    }
                    Box(modifier = Modifier.weight(0.1f)){}
                    // DONE Button
                    Button(
                        modifier = Modifier.weight(0.3f),
                        onClick = {
                            // Persist reminder and close modal
                            onEvent(ReminderEvent.SaveReminder(
                                reminder.copy(time =
                                    // Convert to local date time to zoned date time then finally to instant
                                    ZonedDateTime.of(
                                        LocalDateTime.of(datePickerState.getSelectedDate(), LocalTime.of(timePickerState.hour, timePickerState.minute)),
                                        ZoneId.systemDefault()
                                    ).toInstant()
                                )
                            ))
                            onEvent(ReminderEvent.SetDateTimeModalVisibility(false))
                        }
                    ) {
                        Text("DONE")
                    }
                }

            }
        }
    }
}


@Composable
fun ReminderTextField(reminder: Reminder?, remindersState: RemindersState?, onEvent: (ReminderEvent) -> Unit) {
    if (reminder == null || remindersState == null) {
        return
    }

    // Put + button to initiate writing
    if (remindersState.currentReminderText.text.isEmpty()) {
        Button(onClick = {}) { Text("+") }
    }

    val textFieldValue = rememberTextFieldState()

    TextField(value = remindersState.currentReminderText, onValueChange = {
        onEvent(ReminderEvent.ModifyReminderTextFieldValue(it))
        onEvent(ReminderEvent.SaveReminderWithDebounce(reminder.copy(text = it.text)))
    })

}