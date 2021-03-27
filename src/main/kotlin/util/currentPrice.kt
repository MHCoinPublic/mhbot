package util

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

fun currentPrice(): String? { // получить CoinDesk Bitcoin Price Index
    val client = OkHttpClient()

    val request = Request.Builder()
            .url("https://api.coindesk.com/v1/bpi/currentprice.json")
            .build()
    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")
        return response.body()?.string()
    }
}