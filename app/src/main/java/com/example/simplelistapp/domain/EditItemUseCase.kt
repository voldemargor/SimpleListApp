package com.example.simplelistapp.domain

class EditItemUseCase(private val repository: Repository) {

    suspend fun editItem(item: Item) {
        repository.editItem(item)
    }

}