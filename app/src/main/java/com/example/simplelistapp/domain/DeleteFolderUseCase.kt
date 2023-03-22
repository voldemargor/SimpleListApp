package com.example.simplelistapp.domain

class DeleteFolderUseCase(private val repository: Repository) {

    suspend fun deleteFolder(folder: Folder) {
        repository.deleteFolder(folder)
    }

}