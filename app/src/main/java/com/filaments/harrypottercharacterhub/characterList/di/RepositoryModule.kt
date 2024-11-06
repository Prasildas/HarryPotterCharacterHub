package com.filaments.harrypottercharacterhub.characterList.di

import com.filaments.harrypottercharacterhub.characterList.data.repository.CharacterListRepositoryImpl
import com.filaments.harrypottercharacterhub.characterList.domain.repository.CharacterListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by prasildas
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCharacterRepository(
        characterRepositoryImpl: CharacterListRepositoryImpl
    ): CharacterListRepository
}