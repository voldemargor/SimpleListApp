package com.example.simplelistapp.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.example.simplelistapp.domain.Folder
import com.example.simplelistapp.domain.Item
import com.example.simplelistapp.domain.Repository

class DbRepositoryImpl(application: Application) : Repository {

    private val appDao = AppDatabase.getInstance(application).appDao()

    override fun getFoldersList(): LiveData<List<Folder>> =
        MediatorLiveData<List<Folder>>().apply {
            addSource(appDao.getFoldersList()) {
                value = DbMapper.mapListDbModelToListEntity(it)
            }
        }

    override fun getItemsForFolder(folderId: Int): LiveData<List<Item>> =
        Transformations.map(appDao.getItemsForFolder(folderId)) {
            Log.d("mylog", "DbRepositoryImpl: folderId: $folderId, List size: ${it.size}")
            DbMapper.mapListDbModelToListEntity(it)
        }

    override suspend fun getFolder(folderId: Int): Folder {
        val dbModel = appDao.getFolder(folderId)
        return DbMapper.mapDbModelToEntity(dbModel)
    }

    override suspend fun getItem(itemId: Int): Item {
        val dbModel = appDao.getItem(itemId)
        return DbMapper.mapDbModelToEntity(dbModel)
    }

    override suspend fun addFolder(folder: Folder) {
        appDao.addFolder(DbMapper.mapEntityToDbModel(folder))
    }

    override suspend fun addItem(item: Item) {
        appDao.addItem(DbMapper.mapEntityToDbModel(item))
    }

    override suspend fun editFolder(folder: Folder) {
        appDao.addFolder(DbMapper.mapEntityToDbModel(folder))
    }

    override suspend fun editItem(item: Item) {
        appDao.addItem(DbMapper.mapEntityToDbModel(item))
    }

    override suspend fun deleteFolder(folder: Folder) {
        appDao.deleteFolderAndItems(folder.id)
    }

    override suspend fun deleteItem(item: Item) {
        appDao.deleteItem(item.id)
    }


}