package com.example.simplelistapp.presentation.items

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.simplelistapp.data.DbRepositoryImpl
import com.example.simplelistapp.domain.*
import kotlinx.coroutines.launch

class ItemsListViewModel(
    application: Application,
    private val folderId: Int
) : AndroidViewModel(application) {

    private val repository = DbRepositoryImpl(application)
    private val getItemsForFolderUseCase = GetItemsForFolderUseCase(repository)
    private val editItemUseCase = EditItemUseCase(repository)
    private val deleteItemUseCase = DeleteItemUseCase(repository)
    private val addItemUseCase = AddItemUseCase(repository)

    val itemsList = getItemsForFolderUseCase.getItemsForFolder(folderId)

    fun changeEnabledState(item: Item) {
        val newItem = item.copy(enabled = !item.enabled)
        viewModelScope.launch() {
            editItemUseCase.editItem(newItem)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            deleteItemUseCase.deleteItem(item)
        }
    }

    fun addItem() {
        viewModelScope.launch {
            addItemUseCase.addItem(Item(folderId, "Айтем", 0))
        }
    }

}