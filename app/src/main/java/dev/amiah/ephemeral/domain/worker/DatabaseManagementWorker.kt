package dev.amiah.ephemeral.domain.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.amiah.ephemeral.domain.DatabaseManager
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

}