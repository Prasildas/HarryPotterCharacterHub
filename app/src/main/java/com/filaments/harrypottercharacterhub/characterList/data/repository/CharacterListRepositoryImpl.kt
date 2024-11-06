package com.filaments.harrypottercharacterhub.characterList.data.repository

import coil.network.HttpException
import com.filaments.harrypottercharacterhub.characterList.data.local.database.AppDatabase
import com.filaments.harrypottercharacterhub.characterList.data.mappers.toCharacter
import com.filaments.harrypottercharacterhub.characterList.data.mappers.toCharacterEntity
import com.filaments.harrypottercharacterhub.characterList.data.remote.api.HarryPotterApiService
import com.filaments.harrypottercharacterhub.characterList.domain.model.Character
import com.filaments.harrypottercharacterhub.characterList.domain.repository.CharacterListRepository
import com.filaments.harrypottercharacterhub.characterList.utils.Resource
import com.filaments.harrypottercharacterhub.characterList.utils.StringProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

/**
 * Created by prasildas
 */
class CharacterListRepositoryImpl @Inject constructor(
    private val harryPotterApiService: HarryPotterApiService,
    private val appDatabase: AppDatabase,
    private val stringProvider: StringProvider // Inject the StringProvider
) : CharacterListRepository {

    override suspend fun getCharacterList(forceFetchRemote: Boolean): Flow<Resource<List<Character>>> =
        flow {
            emit(Resource.Loading()) // Indicate that loading has started

            // Retrieve local data
            val localCharacterList = appDatabase.characterDao.getAllCharacters()

            // Emit local data if available and if not forced to fetch from remote
            if (localCharacterList.isNotEmpty() && !forceFetchRemote) {
                emit(Resource.Success(data = localCharacterList.map { it.toCharacter() }))
                return@flow // Return early if local data is available
            }

            // Fetch remote data if local data is unavailable or forced to refresh
            val characterListFromApi = try {
                harryPotterApiService.getCharacters()
            } catch (e: IOException) {
                emit(Resource.Error(stringProvider.networkError())) // Use StringProvider for error message
                return@flow // Return early after handling error
            } catch (e: HttpException) {
                emit(Resource.Error(stringProvider.serverError())) // Use StringProvider for error message
                return@flow // Return early after handling error
            } catch (e: Exception) {
                emit(Resource.Error(stringProvider.unknownError())) // Use StringProvider for error message
                return@flow // Return early after handling error
            }

            // Update database with new data from the API
            val characterEntities = characterListFromApi.map { it.toCharacterEntity() }
            appDatabase.characterDao.upsertCharactersList(characterEntities)

            // Emit the updated list from the remote source
            emit(Resource.Success(data = characterEntities.map { it.toCharacter() }))
        }
}

