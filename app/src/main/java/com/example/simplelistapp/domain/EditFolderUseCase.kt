package com.example.simplelistapp.domain

class EditFolderUseCase(private val repository: Repository) {

    fun editFolder(folder: ItemsFolder) {
        repository.editFolder(folder)
    }

}