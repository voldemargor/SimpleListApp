package com.example.simplelistapp.presentation.items

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplelistapp.domain.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemsListViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var getItemsForFolderUseCase: GetItemsForFolderUseCase
    @Inject lateinit var editItemUseCase: EditItemUseCase
    @Inject lateinit var deleteItemUseCase: DeleteItemUseCase
    @Inject lateinit var getFolderUseCase: GetFolderUseCase
    @Inject lateinit var editFolderUseCase: EditFolderUseCase

    lateinit var itemsList: LiveData<List<Item>>

    fun initItemsLD(folderId: Int) {
        viewModelScope.launch {
            itemsList = getItemsForFolderUseCase.getItemsForFolder(folderId)
        }
    }

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