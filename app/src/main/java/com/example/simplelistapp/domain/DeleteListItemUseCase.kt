package com.example.simplelistapp.domain

class DeleteListItemUseCase(private val repository: Repository) {

    fun deleteListItem(listItem: ListItem) {
        repository.deleteListItem(listItem)
    }

}