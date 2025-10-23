package com.gwabs.getpayedpos.domain.location

data class LocationResult(
    val latitude: Double?,
    val longitude: Double?,

    val source: Source
) {
    enum class Source { FRESH_UPDATE, LAST_KNOWN, NONE }
    val isAvailable: Boolean get() = latitude != null && longitude != null
}

interface LocationProvider {

    suspend fun getLocationOrNull(timeoutMillis: Long = 5_000): LocationResult
}

interface LocationPermissionChecker {
    fun hasLocationPermission(): Boolean
}