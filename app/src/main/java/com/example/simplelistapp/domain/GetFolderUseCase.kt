package com.example.simplelistapp.domain

class GetFolderUseCase(private val repository: Repository) {

    suspend fun getFolder(folderId: Int): Folder {
        return repository.getFolder(folderId)
    }

}