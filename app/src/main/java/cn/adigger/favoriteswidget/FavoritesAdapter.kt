package cn.adigger.favoriteswidget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.adigger.favoriteswidget.databinding.ItemFavoriteBinding

class FavoritesAdapter : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {
    private val items = mutableListOf<FavoriteRow>()

    fun submitList(rows: List<FavoriteRow>) {
        items.clear()
        items.addAll(rows)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class FavoriteViewHolder(private val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FavoriteRow) {
            binding.lineText.text = item.line
            binding.tagText.text = item.tag.ifBlank { "收藏" }
            binding.subtitleText.text = item.subtitle
            binding.statusText.text = item.status.ifBlank { "暂无实时信息" }
            binding.nextStatusText.text = item.nextStatus.ifBlank { "" }
        }
    }
}
