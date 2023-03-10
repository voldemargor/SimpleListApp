package com.example.simplelistapp.domain

class DeleteFolderUseCase(private val repository: Repository) {

    fun deleteFolder(folder: Folder) {
        repository.deleteFolder(folder)
    }

}