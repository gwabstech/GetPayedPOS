package com.gwabs.getpayedpos.net

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.work.WorkManager
import com.gwabs.getpayedpos.worker.enqueueOneOffSync

class ConnectivityMonitor(
    context: Context,
    private val workManager: WorkManager
) {
    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            // Only trigger if we have actual internet
            val caps = cm.getNetworkCapabilities(network)
            val hasNet = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
            if (hasNet) enqueueOneOffSync(workManager)
        }
    }

    fun register() {
        cm.registerDefaultNetworkCallback(callback)
    }

    fun unregister() {
        cm.unregisterNetworkCallback(callback)
    }
}