package com.example.simplelistapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.simplelistapp.R
import com.example.simplelistapp.domain.Folder

class FoldersListAdapter : ListAdapter<Folder, FolderViewHolder>(FolderItemDiffCallback()) {

    var onFolderClickListener: ((Folder) -> Unit)? = null
    var onFolderLongClickListener: ((Folder) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.folder_card_enabled, parent, false)
        return FolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folder = getItem(position)
        holder.tvName.text = folder.name
        holder.tvCount.text = folder.itemsCount.toString()
        holder.itemView.setOnClickListener {
            onFolderClickListener?.invoke(folder)
        }
        holder.itemView.setOnLongClickListener {
            onFolderLongClickListener?.invoke(folder)
            true
        }
    }

}