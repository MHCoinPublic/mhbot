package util.mhApi

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.Socket

class socket { // socket api мх коина
    fun regApi(words: List<String>): List<String> { // регистрирование аккаунта
        val client = OkHttpClient()
        var txt = "1" as String?

        val request = Request.Builder()
                .url("https://mhcoin.s3.filebase.com/Server.txt")
                .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            txt = response.body()?.string()
        }
        val poolAddress = txt?.lines()?.get(0) as String
        val poolPort = txt?.lines()?.get(1) as String

        val s = Socket(poolAddress, poolPort.toInt())

        val inputStream = s.getInputStream()
        val outputStream = s.getOutputStream()

        val data = ByteArray(256)
        outputStream.write("REGI,${words[1]},${words[2]},${words[3]}".toByteArray())
        inputStream.read(data)
        inputStream.read(data)
        return String(data).split(",")
    }
}