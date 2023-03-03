package com.example.simplelistapp.domain

class DeleteItemUseCase(private val repository: Repository) {

    fun deleteItem(item: Item) {
        repository.deleteItem(item)
    }

}