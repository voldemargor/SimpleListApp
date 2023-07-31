package com.vladimirgorshkov.simplelistapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folders")
data class FolderDbModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val name: String,
    val itemsCompleted: Int,
    val itemsCount: Int
) {

}
