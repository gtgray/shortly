package tk.atna.shortlyapp.presentation.main;

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tk.atna.shortlyapp.databinding.HistoryLiBinding
import tk.atna.shortlyapp.domain.model.ShortenedUrl

class HistoryAdapter(
    context: Context,
    private val onCopy: ((ShortenedUrl) -> Unit),
    private val onDelete: ((ShortenedUrl) -> Unit)
) : ListAdapter<ShortenedUrl, HistoryAdapter.ItemViewHolder>(DiffCallback()) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(inflater, parent, onCopy, onDelete)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class ItemViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        private val onCopy: ((ShortenedUrl) -> Unit),
        private val onDelete: ((ShortenedUrl) -> Unit),
        private val binding: HistoryLiBinding = HistoryLiBinding.inflate(inflater, parent, false)
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: ShortenedUrl

        init {
            binding.btnCopy.setOnClickListener { onCopy.invoke(item) }
            binding.btnDelete.setOnClickListener { onDelete.invoke(item) }
        }

        fun bind(newItem: ShortenedUrl) {
            item = newItem
            with(binding) {
                tvOriginal.text = item.originalLink
                tvShortened.text = item.shortLink
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<ShortenedUrl>() {

        override fun areItemsTheSame(oldItem: ShortenedUrl, newItem: ShortenedUrl): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: ShortenedUrl, newItem: ShortenedUrl): Boolean {
            return oldItem == newItem
        }
    }
}