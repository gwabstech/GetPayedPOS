package com.gwabs.getpayedpos.worker

import androidx.work.*
import java.util.concurrent.TimeUnit

object WorkNames {
    const val SYNC_ONE_OFF = "sync_one_off"
    const val SYNC_PERIODIC = "sync_periodic"
    const val TAG_SYNC = "tag_sync"            // <- NEW
}

fun enqueueOneOffSync(wm: WorkManager) {
    val request = OneTimeWorkRequestBuilder<SyncWorker>()
        .addTag(WorkNames.TAG_SYNC)            // <- tag for observation
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
        .build()
    wm.enqueueUniqueWork(WorkNames.SYNC_ONE_OFF, ExistingWorkPolicy.REPLACE, request)
}

fun schedulePeriodicSync(wm: WorkManager) {
    val req = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
        .addTag(WorkNames.TAG_SYNC)            // <- tag for observation
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
        .build()
    wm.enqueueUniquePeriodicWork(
        WorkNames.SYNC_PERIODIC,
        ExistingPeriodicWorkPolicy.UPDATE,
        req
    )
}
