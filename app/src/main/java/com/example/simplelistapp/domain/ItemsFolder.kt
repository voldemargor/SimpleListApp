package com.example.simplelistapp.domain

data class ItemsFolder(
    val name: String,
    val itemsCount: Int,
    var id: Int = UNDEFINED_ID
) {

    companion object {
        const val UNDEFINED_ID = -1
    }

}