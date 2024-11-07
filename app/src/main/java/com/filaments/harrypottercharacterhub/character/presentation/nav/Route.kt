package com.filaments.harrypottercharacterhub.character.presentation.nav

import kotlinx.serialization.Serializable

/**
 * Created by prasildas
 */
sealed interface Route{
    @Serializable
    data object List: Route

    @Serializable
    data class Details(
        val characterID: String
    ): Route
}