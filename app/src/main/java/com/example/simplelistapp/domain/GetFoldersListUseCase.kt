package com.example.simplelistapp.domain

class GetFoldersListUseCase(private val repository: Repository) {

    fun getFoldersList() : List<ItemsFolder> {
        return repository.getFoldersList()
    }

}