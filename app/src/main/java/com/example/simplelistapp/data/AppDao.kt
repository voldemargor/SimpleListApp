package com.example.simplelistapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AppDao {

    @Query("SELECT * FROM folders")
    fun getFoldersList(): LiveData<List<FolderDbModel>>

    @Query("SELECT * FROM folders WHERE id=:folderId LIMIT 1")
    suspend fun getFolder(folderId: Int): FolderDbModel

    @Query("SELECT * FROM items WHERE id=:itemId LIMIT 1")
    suspend fun getItem(itemId: Int): ItemDbModel

    @Query("SELECT * FROM items WHERE folderId=:folderId")
    fun getItemsForFolder(folderId: Int): LiveData<List<ItemDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFolder(folder: FolderDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(item: ItemDbModel)

    @Query("DELETE FROM items WHERE id=:itemId")
    suspend fun deleteItem(itemId: Int)

    @Query("DELETE FROM items WHERE folderId=:folderId")
    suspend fun deleteAllItemsIn(folderId: Int)

    @Query("DELETE FROM folders WHERE id=:folderId")
    suspend fun deleteFolder(folderId: Int)

    @Transaction
    suspend fun deleteFolderAndItems(folderId: Int) {
        deleteAllItemsIn(folderId)
        deleteFolder(folderId)
    }

}