package com.filaments.harrypottercharacterhub.core.api

import com.filaments.harrypottercharacterhub.character.data.remote.model.CharacterDto
import retrofit2.http.GET

interface HarryPotterApiService {

    @GET("characters")
    suspend fun getCharacters(): List<CharacterDto>
}