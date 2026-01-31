package dev.amiah.ephemeral.domain.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dev.amiah.ephemeral.domain.DatabaseManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DatabaseManagementWorker(
    appContext: Context,
    workerParams: WorkerParameters
): CoroutineWorker(appContext, workerParams) {

    @Inject lateinit var dbMan: DatabaseManager

    override suspend fun doWork(): Result {

        dbMan.manageActiveNotes()
        dbMan.manageInactiveNotes()

        return Result.success()

    }

    companion object {

        /**
         * Creates and enqueues periodic work to manage the database.
         * This work starts immediately and replaces the previous worker.
         */
        fun startDatabaseWork(appContext: Context) {
            val databaseManagementPeriodicWorkRequest =
                PeriodicWorkRequestBuilder<DatabaseManagementWorker>(
                    repeatInterval = 2,
                    repeatIntervalTimeUnit = TimeUnit.HOURS,
                )
                    .setConstraints(Constraints(requiresDeviceIdle = true))
                    .build()

            WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(
                uniqueWorkName = "ephemeral.manage.data",
                existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                request = databaseManagementPeriodicWorkRequest
            )
        }

    }

}