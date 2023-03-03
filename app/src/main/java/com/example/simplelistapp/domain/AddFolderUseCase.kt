package com.example.simplelistapp.domain

class AddFolderUseCase(private val repository: Repository) {

    fun addFolder(folder: ItemsFolder) {
        repository.addFolder(folder)
    }

}