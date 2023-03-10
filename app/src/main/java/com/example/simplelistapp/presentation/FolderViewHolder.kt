package com.example.simplelistapp.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.simplelistapp.R

class FolderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvName = view.findViewById<TextView>(R.id.tv_folder_name)
    val tvCount = view.findViewById<TextView>(R.id.tv_folder_count)
}