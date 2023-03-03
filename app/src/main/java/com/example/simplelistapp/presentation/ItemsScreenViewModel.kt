package com.example.simplelistapp.presentation

import androidx.lifecycle.ViewModel
import com.example.simplelistapp.data.TempRepositoryImpl
import com.example.simplelistapp.domain.*

class ItemsScreenViewModel : ViewModel() {

    private val repository = TempRepositoryImpl
    private val getItemsListUseCase = GetItemsForFolderUseCase(repository)
    private val editItemUseCase = EditItemUseCase(repository)
    private val deleteItemUseCase = DeleteItemUseCase(repository)

    private lateinit var folder: ItemsFolder

    val itemsList = getItemsListUseCase.getItemsForFolder(1)

    fun changeEnableState(item: Item) {
        val newItem = item.copy(enabled = !item.enabled)
        editItemUseCase.editItem(newItem)
    }

    fun deleteItem(item: Item) {
        deleteItemUseCase.deleteItem(item)
    }

}