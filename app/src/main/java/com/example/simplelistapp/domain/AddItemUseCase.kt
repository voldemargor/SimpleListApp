package com.example.simplelistapp.domain

class AddItemUseCase(private val repository: Repository) {

    suspend fun addItem(item: Item) {
        repository.addItem(item)
    }

}