package com.filaments.harrypottercharacterhub.character.domain.model

import java.io.Serializable

/**
 * Created by prasildas
 */
data class Character(
    val actor: String,
    val alive: Boolean,
    val dateOfBirth: String?,
    val house: String?,
    val id: String,
    val image: String?,
    val name: String,
    val species: String?
): Serializable