package com.filaments.harrypottercharacterhub.characterList.data.remote.model

data class CharacterDto(
    val actor: String,
    val alive: Boolean,
    val dateOfBirth: String?,
    val house: String?,
    val id: String,
    val image: String?,
    val name: String,
    val species: String?
)