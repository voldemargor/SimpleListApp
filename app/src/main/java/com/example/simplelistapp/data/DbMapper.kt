package com.example.simplelistapp.data

import com.example.simplelistapp.domain.Folder
import com.example.simplelistapp.domain.Item

object DbMapper {

    fun mapEntityToDbModel(item: Item) = ItemDbModel(
        id = item.id,
        folderId = item.folderId,
        name = item.name,
        count = item.count,
        enabled = item.enabled
    )

    fun mapEntityToDbModel(folder: Folder) = FolderDbModel(
        id = folder.id,
        name = folder.name,
        itemsCompleted = folder.itemsCompleted,
        itemsCount = folder.itemsCount
    )

    fun mapDbModelToEntity(itemDbModel: ItemDbModel) = Item(
        id = itemDbModel.id,
        folderId = itemDbModel.folderId,
        name = itemDbModel.name,
        count = itemDbModel.count,
        enabled = itemDbModel.enabled
    )

    fun mapDbModelToEntity(folderDbModel: FolderDbModel) = Folder(
        id = folderDbModel.id,
        name = folderDbModel.name,
        itemsCompleted = folderDbModel.itemsCompleted,
        itemsCount = folderDbModel.itemsCount
    )

    fun mapListDbModelToListEntity(list: List<ItemDbModel>) = list.map {
        mapDbModelToEntity(it)
    }

    @JvmName("mapListDbModelToListEntityFolders")
    fun mapListDbModelToListEntity(list: List<FolderDbModel>) = list.map {
        mapDbModelToEntity(it)
    }


}