package com.example.simplelistapp.domain

class GetItemsForListCardUseCase(private val repository: Repository) {

    fun getItemsForListCard(listCardId: Int): List<ListItem> {
        return repository.getItemsForListCard(listCardId)
    }

}