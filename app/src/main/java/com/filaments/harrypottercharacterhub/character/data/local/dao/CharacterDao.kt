package com.filaments.harrypottercharacterhub.character.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.filaments.harrypottercharacterhub.character.data.local.entities.CharacterEntity

/**
 * Created by prasildas
 */
@Dao
interface CharacterDao {

    @Upsert
    suspend fun upsertCharactersList(characters: List<CharacterEntity>)

    @Query("SELECT * FROM characters")
    suspend fun getAllCharacters(): List<CharacterEntity>

}