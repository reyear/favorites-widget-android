package cn.adigger.favoriteswidget

import android.content.Context
import com.google.gson.Gson

object FavoritesStore {
    private const val PREFS = "favorites_widget"
    private const val KEY_RESPONSE = "response_json"
    private const val KEY_ERROR = "error"
    private val gson = Gson()

    fun saveResponse(context: Context, response: FavoritesResponse) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_RESPONSE, gson.toJson(response))
            .remove(KEY_ERROR)
            .apply()
    }

    fun saveError(context: Context, message: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_ERROR, message)
            .apply()
    }

    fun loadResponse(context: Context): FavoritesResponse? {
        val json = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_RESPONSE, null)
            ?: return null
        return gson.fromJson(json, FavoritesResponse::class.java)
    }

    fun loadError(context: Context): String? {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_ERROR, null)
    }
}
