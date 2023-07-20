package com.example.simplelistapp.domain

import javax.inject.Inject

class AddFolderUseCase @Inject constructor(private val repository: Repository) {

    suspend fun addFolder(folder: Folder): Long {
        return repository.addFolder(folder)
    }

}