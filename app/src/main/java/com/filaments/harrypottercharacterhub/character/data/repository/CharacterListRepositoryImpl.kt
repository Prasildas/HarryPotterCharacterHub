package com.filaments.harrypottercharacterhub.character.data.repository

import com.filaments.harrypottercharacterhub.character.data.local.dao.CharacterDao
import com.filaments.harrypottercharacterhub.character.data.mappers.toCharacter
import com.filaments.harrypottercharacterhub.character.data.mappers.toCharacterEntity
import com.filaments.harrypottercharacterhub.character.domain.model.Character
import com.filaments.harrypottercharacterhub.character.domain.repository.CharacterListRepository
import com.filaments.harrypottercharacterhub.core.api.CustomAppException
import com.filaments.harrypottercharacterhub.core.api.HarryPotterApiService
import com.filaments.harrypottercharacterhub.core.api.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by prasildas
 */
class CharacterListRepositoryImpl @Inject constructor(
    private val harryPotterApiService: HarryPotterApiService,
    private val characterDao: CharacterDao
) : CharacterListRepository {

    override suspend fun getCharacterList(forceFetchRemote: Boolean): Flow<Resource<List<Character>>> =
        flow {
            emit(Resource.Loading())

            val localCharacterList = characterDao.getAllCharacters()

            if (localCharacterList.isNotEmpty() && !forceFetchRemote) {
                emit(Resource.Success(data = localCharacterList.map { it.toCharacter() }))
            } else {
                try {
                    val characterListFromApi = harryPotterApiService.getCharacters()
                    val characterEntities = characterListFromApi.map { it.toCharacterEntity() }

                    characterDao.upsertCharactersList(characterEntities)

                    emit(Resource.Success(data = characterEntities.map { it.toCharacter() }))
                } catch (e: CustomAppException) {
                    emit(Resource.Error(e.message ?: "Unknown Error"))
                }
            }
        }
}