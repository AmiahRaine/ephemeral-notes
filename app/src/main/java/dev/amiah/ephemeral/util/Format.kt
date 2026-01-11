package dev.amiah.ephemeral.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

abstract class Format {

    companion object {

        /**
         * Utility method to convert values from a time picker into a human readable string.
         * Since this expects values from a time picker, it does not check whether values are
         * within a valid range.
         */
        fun hourMinToHumanString(hour: Int, minute: Int, is24Hour: Boolean = false): String {
            if (is24Hour) {
                return "${hour}:${String.format("%02d", minute)}"
            }
            // If PM subtract 12
            if (hour > 12) {
                return "${hour - 12}:${String.format("%02d", minute)} PM"
            }
            // If exactly 12 do not subtract
            if (hour == 12) {
                return "12:${String.format("%02d", minute)} PM"
            }
            // If midnight change 0 to 12
            if (hour == 0) {
                return "12:${String.format("%02d", minute)} AM"
            }
            // Else AM
            return "${hour}:${String.format("%02d", minute)} AM"
        }

        /**
         * Utility method to convert values from a date picker into a human readable string.
         * Since this expects values from a date picker, it does not check whether values are
         * within a valid range.
         */
        fun localDateToHumanString(date: LocalDate?): String {
            if (date == null) return "Unknown date" // TODO: Replace with r string
            // TODO: Date format preferences
            return date.format(DateTimeFormatter.ofPattern("E, MMM d, yyyy"))

        }



    }

}