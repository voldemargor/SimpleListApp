package com.example.simplelistapp.domain

class AddFolderUseCase(private val repository: Repository) {

    suspend fun addFolder(folder: Folder): Long {
        return repository.addFolder(folder)
    }

}