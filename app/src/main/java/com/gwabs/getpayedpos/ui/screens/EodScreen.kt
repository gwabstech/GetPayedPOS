package com.gwabs.getpayedpos.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import com.gwabs.getpayedpos.ui.common.ResultState
import com.gwabs.getpayedpos.ui.eod.EodReportViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EodScreen(vm: EodReportViewModel = koinViewModel()) {
    val state by vm.state.collectAsState()
    LaunchedEffect(Unit) { vm.loadToday() }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        when (val s = state) {
            is ResultState.Loading -> LinearProgressIndicator()
            is ResultState.Failure -> Text("Error: ${s.message}", color = MaterialTheme.colorScheme.error)
            is ResultState.Success -> {
                val r = s.data
                Text("EOD Report", style = MaterialTheme.typography.headlineSmall)
                ElevatedCard {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Cash: ${r.cash.count} • ₦%,.2f".format(r.cash.sum))
                        Text("Card: ${r.card.count} • ₦%,.2f".format(r.card.sum))
                        Text("Incoming transfers: ₦%,.2f".format(r.incomingTransfersSum))
                        Divider()
                        Text("Pending: ${r.pendingCount} • ₦%,.2f".format(r.pendingSum))
                        Text("Synced: ${r.syncedCount} • ₦%,.2f".format(r.syncedSum))
                        Divider()
                        Text("Starting balance: ₦%,.2f".format(r.startingBalance))
                        Text("Ending balance:   ₦%,.2f".format(r.endingBalance))
                    }
                }
            }
            else -> {}
        }
    }
}
