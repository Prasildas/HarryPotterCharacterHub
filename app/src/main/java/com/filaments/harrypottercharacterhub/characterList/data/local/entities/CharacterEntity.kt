package com.filaments.harrypottercharacterhub.characterList.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by prasildas
 */
@Entity(tableName = "characters")
data class CharacterEntity(
    val actor: String,
    val alive: Boolean,
    val dateOfBirth: String?,
    val house: String?,
    @PrimaryKey val id: String,
    val image: String?,
    val name: String,
    val species: String?
)