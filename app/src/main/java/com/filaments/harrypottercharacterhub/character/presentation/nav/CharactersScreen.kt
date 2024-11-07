package com.filaments.harrypottercharacterhub.character.presentation.nav

/**
 * Created by prasildas
 */
sealed class CharactersScreen(val route: String) {
    object Home : CharactersScreen("character_list")
    object Details : CharactersScreen("character_detail/{characterId}") {
        fun createRoute(characterId: String) = "character_detail/$characterId"
    }
}

