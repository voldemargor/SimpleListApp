package com.example.simplelistapp.domain

import androidx.lifecycle.LiveData
import javax.inject.Inject

class GetItemsForFolderUseCase @Inject constructor(private val repository: Repository) {

    fun getItemsForFolder(folderId: Int): LiveData<List<Item>> {
        return repository.getItemsForFolder(folderId)
    }

}