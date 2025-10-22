package com.gwabs.getpayedpos

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.gwabs.getpayedpos.data.di.databaseModule
import com.gwabs.getpayedpos.domain.di.domainModule
import com.gwabs.getpayedpos.domain.location.locationModule
import com.gwabs.getpayedpos.net.ConnectivityMonitor
import com.gwabs.getpayedpos.net.di.connectivityModule
import com.gwabs.getpayedpos.network.di.networkModule
import com.gwabs.getpayedpos.ui.di.viewModelModule
import com.gwabs.getpayedpos.worker.di.workModule
import com.gwabs.getpayedpos.worker.schedulePeriodicSync
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import org.koin.core.context.startKoin

class App : Application(), Configuration.Provider {

    // Use a lazy factory; no need to resolve from Koin
    private val koinWorkerFactory by lazy { KoinWorkerFactory() }

    // ✅ WorkManager 2.9.x+ expects a *property* override
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(koinWorkerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidContext(this@App)
            modules(
                databaseModule,
                domainModule,
                networkModule,
                workModule,
                locationModule,
                connectivityModule,
                viewModelModule
            )

        }

        // ✅ Call this AFTER startKoin so the bean exists
        get<ConnectivityMonitor>().register()

        // Optional: schedule periodic sync on app start
        WorkManager.getInstance(this).also { wm ->
            schedulePeriodicSync(wm)
        }
    }
}
