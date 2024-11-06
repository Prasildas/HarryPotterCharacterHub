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
    private val stringProvider: StringProvider
) : CharacterListRepository {

    override suspend fun getCharacterList(forceFetchRemote: Boolean): Flow<Resource<List<Character>>> =
        flow {
            emit(Resource.Loading())

            val localCharacterList = appDatabase.characterDao.getAllCharacters()

            if (localCharacterList.isNotEmpty() && !forceFetchRemote) {
                emit(Resource.Success(data = localCharacterList.map { it.toCharacter() }))
                return@flow
            }

            val characterListFromApi = try {
                harryPotterApiService.getCharacters()
            } catch (e: IOException) {
                emit(Resource.Error(stringProvider.networkError()))
                return@flow
            } catch (e: HttpException) {
                emit(Resource.Error(stringProvider.serverError()))
                return@flow
            } catch (e: Exception) {
                emit(Resource.Error(stringProvider.unknownError()))
                return@flow
            }

            val characterEntities = characterListFromApi.map { it.toCharacterEntity() }

            // Check if the new data differs from the existing local data
            val localCharacters = localCharacterList.map { it.toCharacter() }
            if (characterEntities.map { it.toCharacter() } != localCharacters) {
                appDatabase.characterDao.upsertCharactersList(characterEntities)
            }

            // Emit the updated list from the remote source
            emit(Resource.Success(data = characterEntities.map { it.toCharacter() }))
        }
}


