package com.example.simplelistapp.domain

class AddItemUseCase(private val repository: Repository) {

    fun addItem(item: Item) {
        repository.addItem(item)
    }

}