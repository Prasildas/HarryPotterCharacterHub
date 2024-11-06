package com.filaments.harrypottercharacterhub.characterList.presentation

import com.filaments.harrypottercharacterhub.characterList.domain.model.Character

/**
 * Created by prasildas
 */
data class CharacterListState (
    val isLoading: Boolean = false,
    val characterList: List<Character> = emptyList(),
    val errorMessage: String? = null
)