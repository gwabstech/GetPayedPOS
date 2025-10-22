package com.gwabs.getpayedpos.network

import android.util.Log
import com.google.gson.Gson
import com.gwabs.getpayedpos.network.dto.SyncRequest
import com.gwabs.getpayedpos.network.dto.SyncResponse
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer

private const val TAG = "MockSyncInterceptor"

class MockSyncInterceptor(
    private val gson: Gson
) : Interceptor {

    @Volatile var shouldFail: Boolean = false
    @Volatile var networkDelayMs: Long = 600L

    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()


        val path = req.url.encodedPath
        val method = req.method

        Log.d(TAG, "Intercepting request: $method $path")

        if (path == "/sync" && method == "POST") {
            if (networkDelayMs > 0) {
                Log.d(TAG, "Simulating network delay of ${networkDelayMs}ms")
                try { Thread.sleep(networkDelayMs) } catch (_: InterruptedException) {}
            }

            if (shouldFail) {
                Log.w(TAG, "Simulating a failed sync request.")
                val mediaType = "application/json".toMediaTypeOrNull()
                val body = ResponseBody.create(mediaType, """{"error":"mock failure"}""")
                return Response.Builder()
                    .request(req)
                    .protocol(Protocol.HTTP_1_1)
                    .code(500)
                    .message("Mock server error")
                    .body(body)
                    .build()
            }

            Log.d(TAG, "Simulating a successful sync request.")

            val bodyStr = runCatching {
                val buffer = Buffer()
                req.body?.writeTo(buffer)
                buffer.readUtf8()
            }.getOrDefault("{}")

            val syncReq = runCatching {
                Log.d(TAG, "Parsing sync request body: $bodyStr")
                gson.fromJson(bodyStr, SyncRequest::class.java)
            }.getOrNull() ?: SyncRequest(emptyList())

            val syncedIds = syncReq.transactions.map { it.id }
            val respJson = gson.toJson(SyncResponse(syncedIds = syncedIds))

            Log.d(TAG, "Generating mock response: $respJson")

            val mediaType = "application/json".toMediaTypeOrNull()
            val respBody = ResponseBody.create(mediaType, respJson)

            return Response.Builder()
                .request(req)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(respBody)
                .build()
        }


        return chain.proceed(req)
    }
}
