package com.filaments.harrypottercharacterhub.characterList.data.repository

import coil.network.HttpException
import com.filaments.harrypottercharacterhub.characterList.common.Resource
import com.filaments.harrypottercharacterhub.characterList.data.local.database.AppDatabase
import com.filaments.harrypottercharacterhub.characterList.data.mappers.toCharacter
import com.filaments.harrypottercharacterhub.characterList.data.mappers.toCharacterEntity
import com.filaments.harrypottercharacterhub.characterList.data.remote.api.HarryPotterApiService
import com.filaments.harrypottercharacterhub.characterList.domain.model.Character
import com.filaments.harrypottercharacterhub.characterList.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

/**
 * Created by prasildas
 */
class CharacterRepositoryImpl @Inject constructor(
    private val harryPotterApiService: HarryPotterApiService,
    private val appDatabase: AppDatabase
) : CharacterRepository {
    override suspend fun getCharacters(): Flow<Resource<List<Character>>> {
        return flow {
            emit(Resource.Loading(true))

            val localCharacterList = appDatabase.characterDao.getAllCharacters()

            if (localCharacterList.isNotEmpty()) {
                emit(Resource.Success(
                    data = localCharacterList.map { characterEntity ->
                        characterEntity.toCharacter()
                    }
                ))

                emit(Resource.Loading(isLoading = false))
                return@flow
            }

//            Load the data from the remote
            val characterListFromApi = try {
                harryPotterApiService.getCharacters()
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error Loading"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error Loading"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error Loading"))
                return@flow
            }

            val characterEntities = characterListFromApi.let {
                it.map { characterDto ->
                    characterDto.toCharacterEntity()
                }
            }

            appDatabase.characterDao.upsertCharactersList(characterEntities)

            emit(Resource.Success(
                characterEntities.map {
                    it.toCharacter()
                }
            ))
            emit(Resource.Loading(isLoading = false))
        }
    }

}