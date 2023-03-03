package com.example.simplelistapp.domain

class GetItemsForFolderUseCase(private val repository: Repository) {

    fun getItemsForFolder(folderId: Int): List<Item> {
        return repository.getItemsForFolder(folderId)
    }

}