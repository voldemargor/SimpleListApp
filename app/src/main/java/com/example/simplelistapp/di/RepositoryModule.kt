package com.example.simplelistapp.di

import com.example.simplelistapp.data.DbRepositoryImpl
import com.example.simplelistapp.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepository(repository: DbRepositoryImpl): Repository

}