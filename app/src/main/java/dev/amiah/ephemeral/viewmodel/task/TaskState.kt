package dev.amiah.ephemeral.viewmodel.task

import androidx.compose.ui.text.input.TextFieldValue
import dev.amiah.ephemeral.data.entity.Task

data class TaskState(
    val currentTask: Task? = null,
    val currentTaskText: TextFieldValue = TextFieldValue(),
    val currentTaskIsNew: Boolean = false
)
