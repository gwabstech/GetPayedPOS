package com.gwabs.getpayedpos.domain.location

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val locationModule = module {
    single<LocationPermissionChecker> { AndroidLocationPermissionChecker(androidContext()) }
    single<LocationProvider> { FusedLocationProviderImpl(androidContext(), get()) }
}