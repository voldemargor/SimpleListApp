package com.example.simplelistapp.presentation.items

import androidx.recyclerview.widget.DiffUtil
import com.example.simplelistapp.domain.Item

class ItemDiffCallback : DiffUtil.ItemCallback<Item>() {

    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
        oldItem.equals(newItem)

}