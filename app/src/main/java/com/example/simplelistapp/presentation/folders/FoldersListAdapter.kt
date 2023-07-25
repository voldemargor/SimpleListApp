package com.example.simplelistapp.presentation.folders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simplelistapp.databinding.FolderCardBinding
import com.example.simplelistapp.domain.Folder

class FoldersListAdapter : ListAdapter<Folder, FoldersListAdapter.FolderViewHolder>(
    FolderItemDiffCallback()
) {

    var onFolderClickListener: ((Folder) -> Unit)? = null
    var onFolderLongClickListener: ((Folder) -> Unit)? = null
    var onFolderMenuClickListener: ((Folder) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val binding = FolderCardBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return FolderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folder = getItem(position)
        val binding = holder.binding

        binding.tvFolderName.text = folder.name
        val itemsCompleted= folder.itemsCompleted.coerceIn(0, folder.itemsCount)
        binding.tvFolderCount.text = "$itemsCompleted/${folder.itemsCount}"

        binding.root.setOnClickListener {
            onFolderClickListener?.invoke(folder)
        }

        binding.root.setOnLongClickListener {
            onFolderLongClickListener?.invoke(folder)
            true
        }

        binding.btnFolderMenu.setOnClickListener {
            onFolderMenuClickListener?.invoke(folder)
        }

    }

    inner class FolderViewHolder(val binding: FolderCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

}