package com.example.simplelistapp.presentation.folders

import androidx.recyclerview.widget.DiffUtil
import com.example.simplelistapp.domain.Folder

class FolderItemDiffCallback : DiffUtil.ItemCallback<Folder>() {

    override fun areItemsTheSame(oldItem: Folder, newItem: Folder): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Folder, newItem: Folder): Boolean {
        return oldItem.equals(newItem)
    }
}