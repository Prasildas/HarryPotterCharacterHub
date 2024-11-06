package com.filaments.harrypottercharacterhub.characterList.domain.repository

import com.filaments.harrypottercharacterhub.characterList.domain.model.Character
import com.filaments.harrypottercharacterhub.characterList.utils.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Created by prasildas
 */
interface CharacterListRepository {
    suspend fun getCharacterList(
        forceFetchRemote: Boolean
    ): Flow<Resource<List<Character>>>
}