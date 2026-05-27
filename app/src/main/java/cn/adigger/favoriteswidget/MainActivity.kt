package cn.adigger.favoriteswidget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cn.adigger.favoriteswidget.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter = FavoritesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.favoritesRecycler.layoutManager = LinearLayoutManager(this)
        binding.favoritesRecycler.adapter = adapter
        binding.swipeRefresh.setOnRefreshListener { refresh() }

        FavoritesStore.loadResponse(this)?.let { render(it) }
        FavoritesStore.loadError(this)?.let { showError(it) }

        refresh()
    }

    private fun refresh() {
        binding.swipeRefresh.isRefreshing = true
        binding.errorText.visibility = android.view.View.GONE
        lifecycleScope.launch {
            runCatching {
                withContext(Dispatchers.IO) { FavoritesRepository(this@MainActivity).fetch() }
            }.onSuccess { response ->
                FavoritesStore.saveResponse(this@MainActivity, response)
                render(response)
                FavoritesWidgetRenderer.refreshAll(this@MainActivity)
            }.onFailure { error ->
                FavoritesStore.saveError(this@MainActivity, error.message ?: getString(R.string.widget_error))
                showError(error.message ?: getString(R.string.widget_error))
            }
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun render(response: FavoritesResponse) {
        binding.timestampText.text = "更新时间：${response.timestamp}"
        adapter.submitList(response.favorites)
    }

    private fun showError(message: String) {
        binding.errorText.text = message
        binding.errorText.visibility = android.view.View.VISIBLE
    }
}
