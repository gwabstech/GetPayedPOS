package com.gwabs.getpayedpos.domain.location

data class LocationResult(
    val latitude: Double?,
    val longitude: Double?,
    /** where the value came from (for logging/analytics) */
    val source: Source
) {
    enum class Source { FRESH_UPDATE, LAST_KNOWN, NONE }
    val isAvailable: Boolean get() = latitude != null && longitude != null
}

interface LocationProvider {
    /** Tries to get a location quickly. Returns nulls when unavailable or permission denied. */
    suspend fun getLocationOrNull(timeoutMillis: Long = 5_000): LocationResult
}

interface LocationPermissionChecker {
    fun hasLocationPermission(): Boolean
}