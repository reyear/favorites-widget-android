package cn.adigger.favoriteswidget

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class FavoritesRefreshWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return runCatching {
            val response = FavoritesRepository(applicationContext).fetch()
            FavoritesStore.saveResponse(applicationContext, response)
            FavoritesWidgetRenderer.refreshAll(applicationContext)
        }.fold(
            onSuccess = { Result.success() },
            onFailure = {
                FavoritesStore.saveError(applicationContext, it.message ?: applicationContext.getString(R.string.widget_error))
                FavoritesWidgetRenderer.refreshAll(applicationContext)
                Result.retry()
            }
        )
    }
}
