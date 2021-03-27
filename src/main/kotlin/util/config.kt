package util

import com.google.gson.Gson
import json.config
import java.nio.file.Files
import java.nio.file.Paths

private val jsonConfig = try { // читаем конфиг
    val path = Paths.get("config.json")
    Files.readString(path)
} catch (error: Exception) {
    throw RuntimeException("Не найден файл config.json" +
            " , перекачайте файл с репозитория.", error)
}

fun getConfig(): config? {
    val gson = Gson()

    return gson.fromJson(jsonConfig, config::class.java)
}