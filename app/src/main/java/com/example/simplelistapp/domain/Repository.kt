package com.example.simplelistapp.domain

import androidx.lifecycle.LiveData

interface Repository {

    fun getFoldersList(): LiveData<List<Folder>>

    fun getItemsForFolder(folderId: Int): LiveData<List<Item>>

    suspend fun getFolder(folderId: Int): Folder

    suspend fun getItem(itemId: Int): Item

    suspend fun addFolder(folder: Folder)

    suspend fun addItem(item: Item)

    suspend fun editFolder(folder: Folder)

    suspend fun editItem(item: Item)

    suspend fun deleteFolder(folder: Folder)

    suspend fun deleteItem(item: Item)

}