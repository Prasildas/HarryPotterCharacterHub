package com.filaments.harrypottercharacterhub.characterList.data.remote.model

import com.google.gson.annotations.SerializedName

data class CharacterDto(
    @SerializedName("actor") val actor: String,
    @SerializedName("alive") val alive: Boolean,
    @SerializedName("date_of_birth") val dateOfBirth: String?,
    @SerializedName("house") val house: String?,
    @SerializedName("id") val id: String,
    @SerializedName("image") val image: String?,
    @SerializedName("name") val name: String,
    @SerializedName("species") val species: String?
)