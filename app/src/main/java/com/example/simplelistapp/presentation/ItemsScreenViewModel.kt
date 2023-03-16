package com.example.simplelistapp.presentation

import androidx.lifecycle.ViewModel
import com.example.simplelistapp.data.TempRepositoryImpl
import com.example.simplelistapp.domain.*

class ItemsScreenViewModel : ViewModel() {

    private val repository = TempRepositoryImpl
    private val getItemsListUseCase = GetItemsForFolderUseCase(repository)
    private val getItemUseCase = GetItemUseCase(repository)
    private val editItemUseCase = EditItemUseCase(repository)
    private val deleteItemUseCase = DeleteItemUseCase(repository)

    private lateinit var folder: Folder

    val itemsList = getItemsListUseCase.getItemsForFolder(1)

    fun getItem(id: Int): Item {
        return getItemUseCase.getItem(id)
    }

    fun changeEnabledState(item: Item) {
        val newItem = item.copy(enabled = !item.enabled)
        editItemUseCase.editItem(newItem)
    }

    fun editItem(item: Item) {
        editItemUseCase.editItem(item)
    }

    fun deleteItem(item: Item) {
        deleteItemUseCase.deleteItem(item)
    }

}