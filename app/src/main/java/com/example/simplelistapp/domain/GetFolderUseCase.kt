package com.example.simplelistapp.domain

class GetFolderUseCase(private val repository: Repository) {

    fun getFolder(folderId: Int): ItemsFolder {
        return repository.getFolder(folderId)
    }

}