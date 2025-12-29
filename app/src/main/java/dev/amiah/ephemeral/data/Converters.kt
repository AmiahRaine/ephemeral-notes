package dev.amiah.ephemeral.data

import androidx.room.TypeConverter
import java.time.Instant

class Converters {

    @TypeConverter
    fun toInstant(instantString: String): Instant {
        return Instant.parse(instantString);
    }

    @TypeConverter
    fun fromInstant(instant: Instant): String {
        return instant.toString();
    }

}