package com.filaments.harrypottercharacterhub.character.presentation.state

import com.filaments.harrypottercharacterhub.character.domain.model.Character

/**
 * Created by prasildas
 */
data class CharacterListState (
    val isLoading: Boolean = false,
    val characterList: List<Character> = emptyList(),
    val errorMessage: String? = null
)