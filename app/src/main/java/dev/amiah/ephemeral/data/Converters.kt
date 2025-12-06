package dev.amiah.ephemeral.data

import androidx.room.TypeConverter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class Converters {

    @TypeConverter
    fun toDate(dateString: String): ZonedDateTime {
        return ZonedDateTime.parse(dateString);
    }

    @TypeConverter
    fun fromDate(date: ZonedDateTime): String {
        return date.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }


}