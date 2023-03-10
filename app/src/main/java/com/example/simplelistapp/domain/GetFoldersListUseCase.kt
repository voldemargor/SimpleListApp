package com.example.simplelistapp.domain

import androidx.lifecycle.LiveData

class GetFoldersListUseCase(private val repository: Repository) {

    fun getFoldersList() : LiveData<List<Folder>> {
        return repository.getFoldersList()
    }

}