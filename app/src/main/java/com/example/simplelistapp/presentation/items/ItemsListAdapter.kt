package com.example.simplelistapp.presentation.items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simplelistapp.R
import com.example.simplelistapp.domain.Item

class ItemsListAdapter : ListAdapter<Item, ItemsListAdapter.ItemViewHolder>(ItemDiffCallback()) {

    var onItemClickListener: ((Item) -> Unit)? = null
    var onItemLongClickListener: ((Item) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        if (item.enabled) return VIEW_TYPE_ENABLED
        return VIEW_TYPE_DISABLED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.item_card_enabled
            VIEW_TYPE_DISABLED -> R.layout.item_card_disabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)

        holder.tvName.text = item.name
        holder.tvCount.text = item.count.toString()

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item)
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.invoke(item)
            true
        }
    }

    inner class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tv_item_name)
        val tvCount: TextView = view.findViewById(R.id.tv_item_count)
    }

    companion object {
        const val VIEW_TYPE_DISABLED = 0
        const val VIEW_TYPE_ENABLED = 1
    }
}
