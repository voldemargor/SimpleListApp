package com.example.simplelistapp.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ItemsViewModelFactory(
    private val folderId: Int,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemsListViewModel::class.java))
            return ItemsListViewModel(application, folderId) as T
        throw RuntimeException("Unknown view model class $modelClass")
    }

}