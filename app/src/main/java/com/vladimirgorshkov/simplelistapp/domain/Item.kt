package com.vladimirgorshkov.simplelistapp.domain

data class Item(
    val folderId: Int,
    val name: String,
    val count: Int,
    val enabled: Boolean = true,
    var id: Int = UNDEFINED_ID
) {

//    override fun toString(): String {
//        return name
//    }

    companion object {
        const val UNDEFINED_ID = 0
    }

}
