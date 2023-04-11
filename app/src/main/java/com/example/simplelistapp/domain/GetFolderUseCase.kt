package com.example.simplelistapp.domain

import javax.inject.Inject

class GetFolderUseCase @Inject constructor(private val repository: Repository) {

    suspend fun getFolder(folderId: Int): Folder {
        return repository.getFolder(folderId)
    }

}