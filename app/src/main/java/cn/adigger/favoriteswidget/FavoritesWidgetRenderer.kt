package cn.adigger.favoriteswidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

object FavoritesWidgetRenderer {
    fun refreshAll(context: Context) {
        val manager = AppWidgetManager.getInstance(context)
        val ids = manager.getAppWidgetIds(ComponentName(context, FavoritesAppWidgetProvider::class.java))
        if (ids.isNotEmpty()) {
            updateWidgets(context, manager, ids)
        }
    }

    fun updateWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val response = FavoritesStore.loadResponse(context)
        val error = FavoritesStore.loadError(context)
        appWidgetIds.forEach { appWidgetId ->
            val views = RemoteViews(context.packageName, R.layout.widget_favorites)
            views.setTextViewText(R.id.widgetTimestamp, response?.timestamp ?: error ?: context.getString(R.string.loading))
            views.setTextViewText(R.id.widgetItems, buildItemsText(context, response, error))
            views.setOnClickPendingIntent(R.id.widgetRefresh, refreshPendingIntent(context))
            views.setOnClickPendingIntent(R.id.widgetTitle, openAppPendingIntent(context))
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    private fun buildItemsText(context: Context, response: FavoritesResponse?, error: String?): String {
        if (error != null && response == null) return error
        val rows = response?.favorites.orEmpty()
        if (rows.isEmpty()) return context.getString(R.string.widget_empty)
        return rows.joinToString("\n\n") { row ->
            "${row.line}  ${row.status}\n${row.subtitle}${if (row.nextStatus.isBlank()) "" else "\n${row.nextStatus}"}"
        }
    }

    private fun refreshPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, FavoritesAppWidgetProvider::class.java).apply {
            action = FavoritesAppWidgetProvider.ACTION_REFRESH
        }
        return PendingIntent.getBroadcast(
            context,
            1001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun openAppPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(
            context,
            1002,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
