package com.gwabs.getpayedpos.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import org.koin.compose.koinInject

@Composable
fun SyncStatusIndicator(
    modifier: Modifier = Modifier,
    tag: String = "tag_sync"
) {
    val wm: WorkManager = koinInject()

    val workInfosFlow = wm.getWorkInfosFlow(
        WorkQuery.Builder
            .fromTags(listOf(tag))
            .build()
    )
    val workInfos by workInfosFlow.collectAsState(initial = emptyList())

    val syncing = workInfos.any { it.state == WorkInfo.State.RUNNING }


    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (syncing) {
            CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
            Text("Syncing…", style = MaterialTheme.typography.bodyMedium)
        } else {
            Icon(Icons.Default.CheckCircle, contentDescription = null)
            Text("Up to date", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
