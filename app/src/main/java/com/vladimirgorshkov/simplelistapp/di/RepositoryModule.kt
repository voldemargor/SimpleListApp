package com.vladimirgorshkov.simplelistapp.di

import com.vladimirgorshkov.simplelistapp.data.DbRepositoryImpl
import com.vladimirgorshkov.simplelistapp.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepository(repository: DbRepositoryImpl): Repository

}