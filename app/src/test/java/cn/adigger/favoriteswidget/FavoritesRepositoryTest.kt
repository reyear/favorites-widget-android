package cn.adigger.favoriteswidget

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FavoritesRepositoryTest {
    @Test
    fun parseFavoritesResponse_extractsFavoriteRows() {
        val json = """
            {
              "timestamp": "2026-05-27 10:39:45+0800",
              "ok": true,
              "mode": "already_favorites",
              "page": "favorites",
              "favorites": [
                {
                  "line": "951路",
                  "tag": "上班",
                  "wait_station": "阳光100",
                  "direction": "开往 体院北公交站",
                  "status": "等待首站发车",
                  "next_status": "点击查看发车预测"
                },
                {
                  "line": "829路",
                  "tag": "上班",
                  "wait_station": "阳光100",
                  "direction": "开往 西站公交站",
                  "status": "3分钟·1站",
                  "next_status": ""
                }
              ]
            }
        """.trimIndent()

        val response = FavoritesRepository.parseResponse(json)

        assertEquals("2026-05-27 10:39:45+0800", response.timestamp)
        assertEquals(2, response.favorites.size)
        assertEquals("951路", response.favorites.first().line)
        assertEquals("3分钟·1站", response.favorites.last().status)
    }

    @Test
    fun favoriteRow_buildsSubtitleFromStationAndDirection() {
        val row = FavoriteRow(
            line = "588路",
            tag = "上班",
            waitStation = "阳光100",
            direction = "开往 西站公交站",
            status = "20分钟·9站",
            nextStatus = "下辆 32分钟·12站"
        )

        assertTrue(row.subtitle.contains("阳光100"))
        assertTrue(row.subtitle.contains("西站公交站"))
    }
}
