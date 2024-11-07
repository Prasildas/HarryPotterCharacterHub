package com.filaments.harrypottercharacterhub.character.domain.repository

import com.filaments.harrypottercharacterhub.character.domain.model.Character
import com.filaments.harrypottercharacterhub.core.api.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Created by prasildas
 */
interface CharacterListRepository {
    suspend fun getCharacterList(
        forceFetchRemote: Boolean
    ): Flow<Resource<List<Character>>>
}