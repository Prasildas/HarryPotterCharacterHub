package com.filaments.harrypottercharacterhub.characterList.data.remote.api

import com.filaments.harrypottercharacterhub.characterList.data.remote.model.CharacterDto
import retrofit2.http.GET

interface HarryPotterApiService {

    @GET("characters")
    suspend fun getCharacters(): List<CharacterDto>
}