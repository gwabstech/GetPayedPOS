package com.gwabs.getpayedpos.network

import com.gwabs.getpayedpos.network.dto.SyncRequest
import com.gwabs.getpayedpos.network.dto.SyncResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface PosApi {
    @POST("sync")
    suspend fun sync(@Body body: SyncRequest): SyncResponse
}