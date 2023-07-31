package com.vladimirgorshkov.simplelistapp.domain

import javax.inject.Inject

class DeleteFolderUseCase @Inject constructor(private val repository: Repository) {

    suspend fun deleteFolder(folder: Folder) {
        repository.deleteFolder(folder)
    }

}