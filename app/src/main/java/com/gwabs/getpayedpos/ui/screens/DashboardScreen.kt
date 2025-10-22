package com.gwabs.getpayedpos.ui.screens

import android.media.MediaPlayer
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.work.WorkManager
import org.koin.androidx.compose.koinViewModel
import com.gwabs.getpayedpos.R
import com.gwabs.getpayedpos.ui.common.SyncStatusIndicator
import com.gwabs.getpayedpos.ui.common.UiEvent
import com.gwabs.getpayedpos.ui.dashboard.DashboardViewModel
import com.gwabs.getpayedpos.worker.enqueueOneOffSync
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun DashboardScreen(vm: DashboardViewModel = koinViewModel()) {
    val wm: WorkManager = koinInject()
    val state by vm.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current

    // Prepare sound (place raw/payment_received.mp3 in res/raw/)
    var player by remember { mutableStateOf<MediaPlayer?>(null) }
    DisposableEffect(Unit) {
        player = MediaPlayer.create(ctx, R.raw.payment_received)
        onDispose { player?.release(); player = null }
    }

// ui/screens/DashboardScreen.kt
    LaunchedEffect(Unit) {
        vm.events.collect { ev ->
            when (ev) {
                is UiEvent.PaymentReceived -> {
                    player?.start()
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "₦%,.2f received from %s (%s) • %s".format(
                                ev.amount,
                                ev.fromAccount,
                                ev.fromBankName,
                                ev.fromBankName
                            ),
                            withDismissAction = true
                        )
                    }
                }
                is UiEvent.Error -> {
                    scope.launch { snackbarHostState.showSnackbar(ev.message) }
                }
            }
        }
    }


    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { inner ->
        Column(
            Modifier.fillMaxSize().padding(inner).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SyncStatusIndicator(modifier = Modifier.padding(top = 8.dp))
            Text("Current Balance", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Text("₦%,.2f".format(state.balance), style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(24.dp))
            Text("Account Number", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Text(
                text = state.accountNumber,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.clickable { vm.onAccountNumberClicked() }
            )

            Spacer(Modifier.height(24.dp))
            Text("Account Name", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Text(
                text = state.accountName,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.clickable { vm.onAccountNumberClicked() }
            )

            Spacer(Modifier.height(24.dp))
            Text("Bank", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Text(
                text = state.bankName,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.clickable { vm.onAccountNumberClicked() }
            )


            Spacer(Modifier.height(32.dp))
            Button(onClick = { vm.onAccountNumberClicked() }) {
                Text("Simulate Incoming Payment")
            }

            Spacer(Modifier.height(16.dp))
            OutlinedButton(
                onClick = {
                    enqueueOneOffSync(wm)                // ✅ schedule a one-off sync
                    scope.launch {
                        snackbarHostState.showSnackbar("Sync scheduled")
                    }
                }
            ) { Text("Sync now") }
        }
    }
}
