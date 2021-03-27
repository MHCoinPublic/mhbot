package util

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.FileInputStream
import java.io.IOException

val firetoken = firetoken()
val dbid = getConfig()?.firebase?.id

fun dbPut(s : String, id : String): String? { // положить в дб
    val client = OkHttpClient()

    val json = MediaType.parse("application/json")
    val body = RequestBody.create(json, s)
    val request = Request.Builder()
            .url("https://$dbid.firebaseio.com/users/${id}.json?access_token=$firetoken")
            .put(body)
            .build()
    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")
        return response.body()?.string()
    }
}

fun dbGet(id : String): String? { // взять из бд
    val client = OkHttpClient()

    val request = Request.Builder()
            .url("https://$dbid.firebaseio.com/users/${id}.json?access_token=$firetoken")
            .build()
    val response = client.newCall(request).execute()
    if (!response.isSuccessful) throw IOException("Unexpected code $response")
    return response.body()?.string()
}

fun dbDelete(id : String): String? { // удалить из бд
    val client = OkHttpClient()

    val request = Request.Builder()
            .url("https://$dbid.firebaseio.com/users/${id}.json?access_token=$firetoken")
            .delete()
            .build()
    val response = client.newCall(request).execute()
    if (!response.isSuccessful) throw IOException("Unexpected code $response")
    return response.body()?.string()
}

fun firetoken(): String? { // получить OAuth2 токен для firebase
    val serviceAccount = FileInputStream(getConfig()?.firebase?.key)
    val googleCred = GoogleCredential.fromStream(serviceAccount)
    val scoped = googleCred.createScoped(
            listOf(
                    "https://www.googleapis.com/auth/firebase.database",
                    "https://www.googleapis.com/auth/userinfo.email"
            )
    )
    scoped.refreshToken()
    return scoped.accessToken
}