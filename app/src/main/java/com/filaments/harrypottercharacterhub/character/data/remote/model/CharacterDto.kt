package com.filaments.harrypottercharacterhub.character.data.remote.model

import com.google.gson.annotations.SerializedName

data class CharacterDto(
    @SerializedName("actor") val actor: String,
    @SerializedName("alive") val alive: Boolean,
    @SerializedName("dateOfBirth") val dateOfBirth: String?,
    @SerializedName("house") val house: String?,
    @SerializedName("id") val id: String,
    @SerializedName("image") val image: String?,
    @SerializedName("name") val name: String,
    @SerializedName("species") val species: String?
)