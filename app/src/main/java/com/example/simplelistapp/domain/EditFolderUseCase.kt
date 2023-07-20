package com.example.simplelistapp.domain

import javax.inject.Inject

class EditFolderUseCase @Inject constructor(private val repository: Repository) {

    suspend fun editFolder(folder: Folder) {
        repository.editFolder(folder)
    }

}