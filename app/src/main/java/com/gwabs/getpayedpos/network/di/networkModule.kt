package com.gwabs.getpayedpos.network.di

import com.google.gson.Gson
import com.gwabs.getpayedpos.network.MockSyncInterceptor
import com.gwabs.getpayedpos.network.PosApi
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit



val networkModule = module {
    single { Gson() }

    single {
        // Mock first so it short-circuits /sync
        val mock = MockSyncInterceptor(get()).apply {
            shouldFail = false         // set true to simulate 500s
            networkDelayMs = 600L      // tweak latency if you like
        }
        mock
    }

    single {

        OkHttpClient.Builder()
            .addInterceptor(get<MockSyncInterceptor>()) // mock server
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            // Base URL must be valid though it won't actually hit the network.
            .baseUrl("https://mock.local/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    single<PosApi> { get<Retrofit>().create(PosApi::class.java) }
}
