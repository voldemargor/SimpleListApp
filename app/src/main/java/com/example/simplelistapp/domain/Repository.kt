package com.example.simplelistapp.domain

import androidx.lifecycle.LiveData

interface Repository {

    // Folder cases
    fun addFolder(folder: ItemsFolder)

    fun getFolder(folderId: Int): ItemsFolder

    fun editFolder(folder: ItemsFolder)

    fun deleteFolder(folder: ItemsFolder)

    fun getFoldersList(): LiveData<List<ItemsFolder>>

    // Item cases
    fun addItem(item: Item)

    fun getItem(itemId: Int): Item

    fun editItem(item: Item)

    fun deleteItem(item: Item)

    fun getItemsForFolder(folderId: Int): LiveData<List<Item>>

}