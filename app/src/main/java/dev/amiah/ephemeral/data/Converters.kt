package dev.amiah.ephemeral.data

import androidx.room.TypeConverter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.Instant

class Converters {

    @TypeConverter
    fun toDate(dateString: String): ZonedDateTime {
        return ZonedDateTime.parse(dateString);
    }

    @TypeConverter
    fun fromDate(date: ZonedDateTime): String {
        return date.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    @TypeConverter
    fun toInstant(instantString: String): Instant {
        return Instant.parse(instantString);
    }

    @TypeConverter
    fun fromInstant(instant: Instant): String {
        return instant.toString();
    }


}