package com.filaments.harrypottercharacterhub.character.di

import com.filaments.harrypottercharacterhub.character.data.repository.CharacterListRepositoryImpl
import com.filaments.harrypottercharacterhub.character.domain.repository.CharacterListRepository
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