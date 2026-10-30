package com.example.personalfinancetracker.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/** A periodic reminder with the first run approximately 24 hours after leaving the app. */
object ReminderScheduler {
    fun schedule(context: Context) {
        val request = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(24, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "finance_reminder", ExistingPeriodicWorkPolicy.UPDATE, request
        )
    }
}
