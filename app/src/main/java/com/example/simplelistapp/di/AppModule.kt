package com.example.simplelistapp.di

import com.example.simplelistapp.data.DbRepositoryImpl
import com.example.simplelistapp.domain.*
import com.example.simplelistapp.presentation.folders.FoldersScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<Repository> { DbRepositoryImpl(application = get()) }

    factory<AddFolderUseCase> { AddFolderUseCase(repository = get()) }
    factory<AddItemUseCase> { AddItemUseCase(repository = get()) }
    factory<DeleteFolderUseCase> { DeleteFolderUseCase(repository = get()) }
    factory<DeleteItemUseCase> { DeleteItemUseCase(repository = get()) }
    factory<EditFolderUseCase> { EditFolderUseCase(repository = get()) }
    factory<EditItemUseCase> { EditItemUseCase(repository = get()) }
    factory<GetFoldersListUseCase> { GetFoldersListUseCase(repository = get()) }
    factory<GetFolderUseCase> { GetFolderUseCase(repository = get()) }
    factory<GetItemsForFolderUseCase> { GetItemsForFolderUseCase(repository = get()) }
    factory<GetItemUseCase> { GetItemUseCase(repository = get()) }

    viewModel<FoldersScreenViewModel> { FoldersScreenViewModel(application = get()) }


}
