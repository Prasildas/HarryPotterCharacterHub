package com.filaments.harrypottercharacterhub.character.data.mappers

import com.filaments.harrypottercharacterhub.character.data.local.entities.CharacterEntity
import com.filaments.harrypottercharacterhub.character.data.remote.model.CharacterDto
import com.filaments.harrypottercharacterhub.character.domain.model.Character

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

fun CharacterDto.toCharacter(): Character {
    return Character(
        id = id,
        name = name,
        actor = actor,
        house = house,
        dateOfBirth = dateOfBirth,
        alive = alive,
        species = species,
        image = image
    )
}