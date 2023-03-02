package com.example.simplelistapp.domain

class AddListItemUseCase(private val repository: Repository) {

    fun addListItem(listItem: ListItem) {
        repository.addListItem(listItem)
    }

}