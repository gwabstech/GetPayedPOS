package com.gwabs.getpayedpos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.work.WorkManager
import com.gwabs.getpayedpos.ui.history.HistoryFilter
import com.gwabs.getpayedpos.ui.history.HistoryViewModel
import com.gwabs.getpayedpos.worker.enqueueOneOffSync
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(vm: HistoryViewModel = koinViewModel()) {
    val wm: WorkManager = koinInject()
    val snackbar = remember { SnackbarHostState() }
    val state by vm.state.collectAsState()
    val filters = listOf(
        HistoryFilter.ALL, HistoryFilter.CASH, HistoryFilter.CARD,
        HistoryFilter.TRANSFER, HistoryFilter.PENDING, HistoryFilter.SYNCED
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("History") },
                actions = {
                    IconButton(onClick = { enqueueOneOffSync(wm) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Sync now")
                    }
                }
            )

        },
        snackbarHost = { SnackbarHost(snackbar) }


    ) { inner ->
        Column(Modifier.fillMaxSize().padding(inner)) {
            // Filter row
            SingleChoiceSegmentedButtonRow(Modifier.padding(12.dp)) {
                filters.forEachIndexed { i, f ->
                    SegmentedButton(
                        selected = f == state.filter,
                        onClick = { vm.setFilter(f) },
                        shape = SegmentedButtonDefaults.itemShape(i, filters.size)
                    ) { Text(f.name) }
                }
            }

            // List
            val fmt = remember { SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()) }
            LazyColumn(Modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.items) { tx ->
                    ElevatedCard(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            Text("₦%,.2f".format(tx.amount), style = MaterialTheme.typography.titleMedium)
                            Text("${tx.paymentMethod} • ${fmt.format(Date(tx.timestampMillis))}")
                            val badge = if (tx.status.name == "PENDING") "PENDING" else "SYNCED"
                            AssistChip(onClick = {}, label = { Text(badge) })
                        }
                    }
                }
            }
        }
    }

}
