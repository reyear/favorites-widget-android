package cn.adigger.favoriteswidget

import android.content.Context
import com.google.gson.Gson
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL

class FavoritesRepository(private val context: Context) {
    private val gson = Gson()

    fun fetch(): FavoritesResponse {
        val url = URL(context.getString(R.string.favorites_api_url))
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 8000
            readTimeout = 8000
            setRequestProperty("Accept", "application/json")
        }
        connection.connect()
        val body = (if (connection.responseCode in 200..299) {
            connection.inputStream
        } else {
            connection.errorStream
        }).bufferedReader().use(BufferedReader::readText)
        if (connection.responseCode !in 200..299) {
            throw IllegalStateException("HTTP ${connection.responseCode}: $body")
        }
        return parseResponse(body)
    }

    companion object {
        fun parseResponse(json: String): FavoritesResponse {
            return Gson().fromJson(json, FavoritesResponse::class.java)
        }
    }
}
