package util.mhApi

import com.google.gson.Gson
import json.userModel
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException

class rest { // rest api мх коина
    val gson = Gson()
    fun checkLogin(user: String, password : String): String? { // вход в аккаунт, получение токена для дальнейшего взаимодействия с API
        val jsonObject = gson.toJson(userModel(user, password))

        val client = OkHttpClient()

        val json = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(json, jsonObject.toString())
        val request = Request.Builder()
                .url("https://mhc.lapki.dev/api/users/login")
                .post(body)
                .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            return response.body()?.string()
        }
    }
    fun checkInfo(token : String): String? { // получение информации о текущем аккаунте
        val client = OkHttpClient()

        val request = Request.Builder()
                .url("https://mhc.lapki.dev/api/users/me")
                .addHeader("Authorization", token)
                .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            return response.body()?.string()
        }
    }
    fun checkServer(): String? { // информация о сервере и сети MHCoin
        val client = OkHttpClient()

        val request = Request.Builder()
                .url("https://mhc.lapki.dev/api/info")
                .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            return response.body()?.string()
        }
    }
    fun checkRich(): String? { // список 10 самых богатых пользователей
        val client = OkHttpClient()

        val request = Request.Builder()
                .url("https://mhc.lapki.dev/api/rich")
                .get()
                .build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) throw IOException("Unexpected code $response")
        return response.body()?.string()
    }
    fun transfer(token: String, recipient : String, amount : String): String? { // перевод денег другому пользователю
        val jsonObject = gson.toJson(json.transfer(recipient, amount))

        val client = OkHttpClient()

        val json = MediaType.parse("application/json")
        val body = RequestBody.create(json, jsonObject.toString())
        val request = Request.Builder()
                .url("https://mhc.lapki.dev/api/users/transfer")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", token)
                .build()
        val response = client.newCall(request).execute()
        return response.body()?.string()
    }
}