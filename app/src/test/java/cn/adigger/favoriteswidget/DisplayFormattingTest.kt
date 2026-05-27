package cn.adigger.favoriteswidget

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class DisplayFormattingTest {
    @Test
    fun `relative timestamp shows just now for current time`() {
        val now = Instant.parse("2026-05-27T04:40:41Z")
        val timestamp = "2026-05-27 12:40:41+0800"

        assertEquals("刚刚更新", DisplayFormatting.relativeTimestamp(timestamp, now))
    }

    @Test
    fun `relative timestamp shows elapsed minutes`() {
        val now = Instant.parse("2026-05-27T04:45:41Z")
        val timestamp = "2026-05-27 12:40:41+0800"

        assertEquals("5 分钟前更新", DisplayFormatting.relativeTimestamp(timestamp, now))
    }

    @Test
    fun `relative timestamp falls back for invalid input`() {
        val now = Instant.parse("2026-05-27T04:45:41Z")

        assertEquals("更新时间：未知", DisplayFormatting.relativeTimestamp("bad-value", now))
    }

    @Test
    fun `widget text contains all favorite rows`() {
        val response = FavoritesResponse(
            timestamp = "2026-05-27 12:40:41+0800",
            ok = true,
            mode = "already_favorites",
            page = "favorites",
            favorites = listOf(
                FavoriteRow("951路", "上班", "金谷园", "开往 师范大学公交站", "3分钟·1站", ""),
                FavoriteRow("588路", "上班", "阳光100", "开往 静海公交站", "5分钟·2站", ""),
                FavoriteRow("857路", "上班", "丽川道", "开往 丽川道", "即将到站", "下辆 14分钟·5站"),
                FavoriteRow("829路", "上班", "阳光100", "开往 理工大学公交站", "4分钟·2站", "下辆 11分钟·6站")
            )
        )

        val text = DisplayFormatting.widgetItemsText(response)

        assertEquals(4, text.lines().count { it.contains("路") && !it.contains("开往") })
        assert(text.contains("951路"))
        assert(text.contains("588路"))
        assert(text.contains("857路"))
        assert(text.contains("829路"))
    }
}
