package cn.adigger.favoriteswidget

import com.google.gson.annotations.SerializedName

data class FavoriteRow(
    val line: String,
    val tag: String,
    @SerializedName("wait_station") val waitStation: String,
    val direction: String,
    val status: String,
    @SerializedName("next_status") val nextStatus: String
) {
    val subtitle: String
        get() = listOf(waitStation, direction)
            .filter { it.isNotBlank() }
            .joinToString(" · ")
}

data class FavoritesResponse(
    val timestamp: String,
    val ok: Boolean,
    val mode: String,
    val page: String,
    val favorites: List<FavoriteRow>
)
