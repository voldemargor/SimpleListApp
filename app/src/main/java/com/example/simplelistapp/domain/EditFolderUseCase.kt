package com.example.simplelistapp.domain

class EditFolderUseCase(private val repository: Repository) {

    suspend fun editFolder(folder: Folder) {
        repository.editFolder(folder)
    }

}