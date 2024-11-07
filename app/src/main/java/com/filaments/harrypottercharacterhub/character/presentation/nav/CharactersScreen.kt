package com.filaments.harrypottercharacterhub.character.presentation.nav

/**
 * Created by prasildas
 */
sealed class CharactersScreen(val route: String) {
    object Home : CharactersScreen("character_list")
    object Details : CharactersScreen("character_detail?characterJson={characterJson}") {
        fun createRoute(characterJson: String) = "character_detail?characterJson=$characterJson"
    }
}


