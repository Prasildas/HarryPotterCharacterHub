package com.filaments.harrypottercharacterhub.characterList.domain.repository

import com.filaments.harrypottercharacterhub.characterList.common.Resource
import com.filaments.harrypottercharacterhub.characterList.domain.model.Character
import kotlinx.coroutines.flow.Flow

/**
 * Created by prasildas
 */
interface CharacterListRepository {
    suspend fun getCharacterList(
        forceFetchRemote: Boolean
    ): Flow<Resource<List<Character>>>
}