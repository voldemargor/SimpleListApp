package com.example.simplelistapp.domain

import androidx.lifecycle.LiveData

class GetItemsForFolderUseCase(private val repository: Repository) {

    fun getItemsForFolder(folderId: Int): LiveData<List<Item>> {
        return repository.getItemsForFolder(folderId)
    }

}