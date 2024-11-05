package com.filaments.harrypottercharacterhub.characterList.data.mappers

import com.filaments.harrypottercharacterhub.characterList.data.local.entities.CharacterEntity
import com.filaments.harrypottercharacterhub.characterList.data.remote.model.CharacterDto
import com.filaments.harrypottercharacterhub.characterList.domain.model.Character

/**
 * Created by prasildas
 */

fun CharacterDto.toCharacterEntity(): CharacterEntity {
    return CharacterEntity(
        actor = actor,
        alive = alive,
        dateOfBirth = dateOfBirth,
        house = house,
        image = image,
        name = name,
        species = species,
        id = id
    )
}

fun CharacterEntity.toCharacter(): Character {
    return Character(
        actor = actor,
        alive = alive,
        dateOfBirth = dateOfBirth,
        house = house,
        image = image,
        name = name,
        species = species,
        id = id
    )
}