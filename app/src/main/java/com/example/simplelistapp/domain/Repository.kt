package com.example.simplelistapp.domain

import androidx.lifecycle.LiveData

interface Repository {

    // Folder cases
    fun addFolder(folder: Folder)

    fun getFolder(folderId: Int): Folder

    fun editFolder(folder: Folder)

    fun deleteFolder(folder: Folder)

    fun getFoldersList(): LiveData<List<Folder>>

    // Item cases
    fun addItem(item: Item)

    fun getItem(itemId: Int): Item

    fun editItem(item: Item)

    fun deleteItem(item: Item)

    fun getItemsForFolder(folderId: Int): LiveData<List<Item>>

}