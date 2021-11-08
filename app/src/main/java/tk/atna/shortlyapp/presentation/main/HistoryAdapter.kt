package tk.atna.shortlyapp.presentation.main;

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tk.atna.shortlyapp.R
import tk.atna.shortlyapp.databinding.HistoryLiBinding
import tk.atna.shortlyapp.domain.model.ShortenedUrl
import tk.atna.shortlyapp.presentation.model.ShortenedUrlItem

class HistoryAdapter(
    context: Context,
    private val onCopy: ((ShortenedUrl) -> Unit),
    private val onDelete: ((ShortenedUrl) -> Unit)
) : ListAdapter<ShortenedUrlItem, HistoryAdapter.ItemViewHolder>(DiffCallback()) {

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

        private lateinit var item: ShortenedUrlItem

        init {
            binding.btnDelete.setOnClickListener { onDelete.invoke(item.shortenedUrl) }
        }

        fun bind(newItem: ShortenedUrlItem) {
            item = newItem
            with(binding) {
                tvOriginal.text = item.shortenedUrl.originalLink
                tvShortened.text = item.shortenedUrl.shortLink

                val context = itemView.context
                if (item.copiedLink == item.shortenedUrl.shortLink) {
                    btnCopy.text = context.getString(R.string.item_history_btn_copied)
                    btnCopy.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.dark_violet))
                    btnCopy.setOnClickListener(null)
                } else {
                    btnCopy.text = context.getString(R.string.item_history_btn_copy)
                    btnCopy.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.cyan))
                    btnCopy.setOnClickListener { onCopy.invoke(item.shortenedUrl) }
                }
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<ShortenedUrlItem>() {

        override fun areItemsTheSame(oldItem: ShortenedUrlItem, newItem: ShortenedUrlItem): Boolean {
            return oldItem.shortenedUrl.code == newItem.shortenedUrl.code
        }

        override fun areContentsTheSame(oldItem: ShortenedUrlItem, newItem: ShortenedUrlItem): Boolean {
            return oldItem == newItem
        }
    }
}