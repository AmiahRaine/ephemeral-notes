package dev.amiah.ephemeral.ui.element

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.amiah.ephemeral.R
import dev.amiah.ephemeral.data.datastore.ExpirationPolicy
import dev.amiah.ephemeral.viewmodel.userpreference.UserPreferenceEvent
import dev.amiah.ephemeral.viewmodel.userpreference.UserPreferencesState

@Composable
fun ExpirationPolicySelectionMenu(
    userPreferenceState: UserPreferencesState?,
    onEvent: (UserPreferenceEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val expanded = remember { mutableStateOf(false) }

    Surface(
        modifier = modifier,
        shape = (
                if (expanded.value)
                    RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                else
                    RoundedCornerShape(15.dp)
                ),
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {

        Row(modifier = Modifier.clickable(onClick = {expanded.value = !expanded.value})) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                text = userPreferenceState?.userPreferences?.notesExpirationPolicy.toString()
            )

            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                painter = (
                        if (expanded.value)
                            painterResource(R.drawable.small_arrow_down)
                        else
                            painterResource(R.drawable.small_arrow_right)
                        ),
                contentDescription = (
                        if (expanded.value)
                            stringResource(R.string.cd_collapse_menu)
                        else
                            stringResource(R.string.cd_expand_menu)
                        )
            )
        }
        //Popup() TODO: Make custom menu with epic animations lol
        DropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp),

            expanded = expanded.value,
            onDismissRequest = {expanded.value = false}
        ) {
            // Display all options, excluding the currently selected option
            ExpirationPolicy.entries.forEach {
                if (it != userPreferenceState?.userPreferences?.notesExpirationPolicy) {
                    DropdownMenuItem(
                        text = { Text(text = it.toString()) },
                        onClick = {
                            onEvent(UserPreferenceEvent.SetNotesExpirationPolicy(it))
                            expanded.value = false
                        }
                    )
                }
            }

        }
    }


}