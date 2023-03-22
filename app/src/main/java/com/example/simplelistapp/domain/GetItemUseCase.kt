package com.example.simplelistapp.domain

class GetItemUseCase(private val repository: Repository) {

    suspend fun getItem(itemId: Int): Item {
        return repository.getItem(itemId)
    }

}