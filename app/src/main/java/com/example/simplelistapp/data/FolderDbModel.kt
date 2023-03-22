package com.example.simplelistapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folders")
data class FolderDbModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val name: String,
    val itemsCount: Int
) {

}
