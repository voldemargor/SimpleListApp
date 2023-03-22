package com.example.simplelistapp.domain

class DeleteItemUseCase(private val repository: Repository) {

    suspend fun deleteItem(item: Item) {
        repository.deleteItem(item)
    }

}