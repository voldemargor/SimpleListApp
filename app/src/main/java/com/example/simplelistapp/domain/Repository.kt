package com.example.simplelistapp.domain

interface Repository {

    fun addListCard(listCard: ListCard)

    fun getListCard(listCardId:Int) : ListCard

    fun editListCard(newListCard: ListCard)

    fun deleteListCard(listCard: ListCard)

    fun getItemsForListCard(listCardId: Int): List<ListItem>


    fun addListItem(listItem: ListItem)

    fun getListItem(itemId: Int): ListItem

    fun editListItem(newListItem: ListItem)

    fun deleteListItem(listItem: ListItem)

}