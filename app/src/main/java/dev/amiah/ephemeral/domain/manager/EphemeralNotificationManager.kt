package dev.amiah.ephemeral.domain.manager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import dev.amiah.ephemeral.R
import javax.inject.Singleton


@Singleton
class EphemeralNotificationManager {

    companion object {

        const val REMINDERS_CHANNEL_ID = "reminders"

        fun createNotificationChannels(appContext: Context) {

            // Define notification channel
            val remindersChannel = NotificationChannel(
                REMINDERS_CHANNEL_ID,
                appContext.getString(R.string.nc_reminders),
                NotificationManager.IMPORTANCE_HIGH
            )

            // Set notification manager
            val notificationManager = appContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            // Create notification channel
            notificationManager.createNotificationChannel(remindersChannel)
        }
    }

}