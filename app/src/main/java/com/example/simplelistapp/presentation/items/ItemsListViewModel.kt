package com.example.simplelistapp.presentation.items

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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
        viewModelScope.launch() {
            editItemUseCase.editItem(newItem)
            if (!newItem.enabled) incrementCompletedInFolder(newItem.folderId)
            else decrementCompletedInFolder(newItem.folderId)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            decrementItemsCountInFolder(item)
            deleteItemUseCase.deleteItem(item)
        }
    }

    private suspend fun decrementItemsCountInFolder(item: Item) {
        val folder = getFolderUseCase.getFolder(item.folderId)
        val newFolder = if (item.enabled) {
            folder.copy(itemsCount = folder.itemsCount.dec())
        } else {
            folder.copy(
                itemsCount = folder.itemsCount.dec(),
                itemsCompleted = folder.itemsCompleted.dec()
            )
        }
        editFolderUseCase.editFolder(newFolder)
    }

    private suspend fun incrementCompletedInFolder(folderId: Int) {
        val folder = getFolderUseCase.getFolder(folderId)
        val newFolder = folder.copy(itemsCompleted = folder.itemsCompleted.inc())
        editFolderUseCase.editFolder(newFolder)
    }

    private suspend fun decrementCompletedInFolder(folderId: Int) {
        val folder = getFolderUseCase.getFolder(folderId)
        val newFolder = folder.copy(itemsCompleted = folder.itemsCompleted.dec())
        editFolderUseCase.editFolder(newFolder)
    }

}