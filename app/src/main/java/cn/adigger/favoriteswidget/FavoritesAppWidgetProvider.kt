package cn.adigger.favoriteswidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class FavoritesAppWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        FavoritesWidgetRenderer.updateWidgets(context, appWidgetManager, appWidgetIds)
        enqueueRefresh(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_REFRESH) {
            enqueueRefresh(context)
        }
    }

    private fun enqueueRefresh(context: Context) {
        val request = OneTimeWorkRequestBuilder<FavoritesRefreshWorker>().build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            "favorites-refresh",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    companion object {
        const val ACTION_REFRESH = "cn.adigger.favoriteswidget.ACTION_REFRESH"
    }
}
