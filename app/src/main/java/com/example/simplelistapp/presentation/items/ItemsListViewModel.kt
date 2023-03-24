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
    private val getFolderUseCase = GetFolderUseCase(repository)
    private val editFolderUseCase = EditFolderUseCase(repository)

    val itemsList = getItemsForFolderUseCase.getItemsForFolder(folderId)

    fun changeEnabledState(item: Item) {
        val newItem = item.copy(enabled = !item.enabled)
        val enabled = newItem.enabled
        viewModelScope.launch() {
            editItemUseCase.editItem(newItem)
            if (enabled) increaseCompletedInFolder(newItem.folderId)
            else decreaseCompletedInFolder(newItem.folderId)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            decreaseItemsCountInFolder(item.folderId)
            deleteItemUseCase.deleteItem(item)
        }
    }

    private suspend fun decreaseItemsCountInFolder(folderId: Int) {
        val folder = getFolderUseCase.getFolder(folderId)
        var newItemsCompleted = folder.itemsCompleted
        var newItemsCount = folder.itemsCount
        newItemsCompleted--
        newItemsCount--
        val newFolder = folder.copy(itemsCompleted = newItemsCompleted, itemsCount = newItemsCount)
        editFolderUseCase.editFolder(newFolder)
    }

    private suspend fun decreaseCompletedInFolder(folderId: Int) {
        val folder = getFolderUseCase.getFolder(folderId)
        var newItemsCompleted = folder.itemsCompleted
        newItemsCompleted--
        val newFolder = folder.copy(itemsCompleted = newItemsCompleted)
        editFolderUseCase.editFolder(newFolder)
    }

    private suspend fun increaseCompletedInFolder(folderId: Int) {
        val folder = getFolderUseCase.getFolder(folderId)
        var newItemsCompleted = folder.itemsCompleted
        newItemsCompleted++
        val newFolder = folder.copy(itemsCompleted = newItemsCompleted)
        editFolderUseCase.editFolder(newFolder)
    }

}