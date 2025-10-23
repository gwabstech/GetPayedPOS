package com.gwabs.getpayedpos.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import com.gwabs.getpayedpos.ui.common.ResultState
import com.gwabs.getpayedpos.ui.sale.AddSaleViewModel

@Composable
fun AddSaleScreen(vm: AddSaleViewModel = koinViewModel()) {
    var amountText by remember { mutableStateOf("") }
    val state by vm.state.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
    //  no need for the result here
    }

    fun requestLocationIfNeeded() {
        permissionLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ))
    }

    fun parseAmount(): Double? = amountText.replace(",", "").toDoubleOrNull()

    Column(Modifier.fillMaxSize().padding(24.dp)) {
        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it },
            label = { Text("Amount (₦)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {
                requestLocationIfNeeded()
                parseAmount()?.let { vm.addCash(it) }
            }, modifier = Modifier.weight(1f)) { Text("Cash") }

            Button(onClick = {
                requestLocationIfNeeded()
                parseAmount()?.let { vm.addCard(it) }
            }, modifier = Modifier.weight(1f)) { Text("Card") }
        }

        Spacer(Modifier.height(12.dp))
        Button(onClick = {
            requestLocationIfNeeded()
            parseAmount()?.let { vm.addCard(it) } // or treat as sale + mark method TRANSFER below
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Transfer (record sale)")
        }

        // If you want outgoing/recorded transfer to be its own method:
        // parseAmount()?.let { vm.addSaleWithMethod(it, PaymentMethod.TRANSFER) }

        Spacer(Modifier.height(24.dp))
        when (val s = state.lastSubmit) {
            is ResultState.Loading -> LinearProgressIndicator()
            is ResultState.Success -> Text("Saved ✅", color = MaterialTheme.colorScheme.primary)
            is ResultState.Failure -> Text("Error: ${s.message}", color = MaterialTheme.colorScheme.error)
            else -> {}
        }
    }
}
