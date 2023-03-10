package com.example.simplelistapp.domain

class GetFolderUseCase(private val repository: Repository) {

    fun getFolder(folderId: Int): Folder {
        return repository.getFolder(folderId)
    }

}