package dev.amiah.ephemeral.ui.element

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.amiah.ephemeral.R

@Composable
fun ActionBar() {
    Row(modifier = Modifier.fillMaxWidth().padding(4.dp)) {

        Box(modifier = Modifier.weight(0.1f))

        // See all reminders
        IconButton(onClick = {}) {
            Icon(
                painterResource(R.drawable.see_reminders),
                contentDescription = "",
            )
        }
        // See all task notes
        IconButton(onClick = {}) {
            Icon(
                painterResource(R.drawable.see_notes),
                contentDescription = "",
            )
        }
        // Settings
        IconButton(onClick = {}) {
            Icon(
                painterResource(R.drawable.gear),
                contentDescription = "",
            )
        }
    }
}