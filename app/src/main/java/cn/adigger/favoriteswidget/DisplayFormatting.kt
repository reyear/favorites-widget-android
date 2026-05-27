package cn.adigger.favoriteswidget

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.max

object DisplayFormatting {
    private val apiFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZ")

    fun relativeTimestamp(timestamp: String?, now: Instant = Instant.now()): String {
        if (timestamp.isNullOrBlank()) return "更新时间：未知"
        return runCatching {
            val updatedAt = OffsetDateTime.parse(timestamp, apiFormatter).toInstant()
            val minutes = max(0L, (now.epochSecond - updatedAt.epochSecond) / 60)
            when {
                minutes <= 0L -> "刚刚更新"
                else -> "${minutes} 分钟前更新"
            }
        }.getOrElse { "更新时间：未知" }
    }

    fun activityTimestamp(timestamp: String?, now: Instant = Instant.now()): String {
        val relative = relativeTimestamp(timestamp, now)
        if (timestamp.isNullOrBlank()) return relative
        val local = runCatching {
            OffsetDateTime.parse(timestamp, apiFormatter)
                .atZoneSameInstant(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("HH:mm"))
        }.getOrNull()
        return if (local == null || relative == "更新时间：未知") relative else "$relative · $local"
    }

    fun widgetItemsText(response: FavoritesResponse?): String {
        val rows = response?.favorites.orEmpty()
        return rows.joinToString("\n") { row ->
            buildString {
                append("${row.line} ${row.status}")
                val detailParts = buildList {
                    if (row.waitStation.isNotBlank()) add(row.waitStation)
                    if (row.direction.isNotBlank()) add(row.direction)
                    if (row.nextStatus.isNotBlank()) add(row.nextStatus)
                }
                if (detailParts.isNotEmpty()) {
                    append("\n")
                    append(detailParts.joinToString(" · "))
                }
            }
        }
    }
}
