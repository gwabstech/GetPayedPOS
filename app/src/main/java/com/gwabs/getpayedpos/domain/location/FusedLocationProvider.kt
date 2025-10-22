package com.gwabs.getpayedpos.domain.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationResult as GmsLocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

class AndroidLocationPermissionChecker(
    private val context: Context
) : LocationPermissionChecker {
    override fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }
}

class FusedLocationProviderImpl(
    private val context: Context,
    private val permissionChecker: LocationPermissionChecker
) : LocationProvider {

    private val client by lazy { LocationServices.getFusedLocationProviderClient(context) }

    @SuppressLint("MissingPermission")
    override suspend fun getLocationOrNull(timeoutMillis: Long): LocationResult {
        if (!permissionChecker.hasLocationPermission()) {
            return LocationResult(null, null, LocationResult.Source.NONE)
        }

        // 1) Try last known (fast)
        val last = runCatching { client.awaitLastLocationOrNull() }.getOrNull()
        if (last != null) {
            return LocationResult(last.latitude, last.longitude, LocationResult.Source.LAST_KNOWN)
        }

        // 2) Request a fresh single update (with timeout)
        val request = LocationRequest.Builder(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            1_000L
        )
            .setMaxUpdates(1)
            .setMinUpdateIntervalMillis(0)
            .build()

        val fresh = withTimeoutOrNull(timeoutMillis) {
            client.awaitSingleUpdate(request)
        } ?: return LocationResult(null, null, LocationResult.Source.NONE)

        return LocationResult(fresh.latitude, fresh.longitude, LocationResult.Source.FRESH_UPDATE)
    }
}

/** --- coroutine helpers --- */

// Helper is on the CLIENT (not on Task), so call: client.awaitLastLocationOrNull()
@SuppressLint("MissingPermission")
private suspend fun FusedLocationProviderClient.awaitLastLocationOrNull(): Location? =
    suspendCancellableCoroutine { cont ->
        lastLocation
            .addOnSuccessListener { cont.resume(it) }
            .addOnFailureListener { cont.resume(null) }
        cont.invokeOnCancellation { /* no-op */ }
    }

@SuppressLint("MissingPermission")
private suspend fun FusedLocationProviderClient.awaitSingleUpdate(request: LocationRequest): Location =
    suspendCancellableCoroutine { cont ->
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: GmsLocationResult) { // <- use GMS type
                removeLocationUpdates(this)
                val loc = result.lastLocation
                if (loc != null && !cont.isCompleted) cont.resume(loc)
            }
        }
        requestLocationUpdates(request, callback, /* looper */ null)
        cont.invokeOnCancellation { removeLocationUpdates(callback) }
    }
