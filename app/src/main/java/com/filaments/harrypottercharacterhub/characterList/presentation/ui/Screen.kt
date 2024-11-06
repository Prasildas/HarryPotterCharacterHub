package com.filaments.harrypottercharacterhub.characterList.presentation.ui

/**
 * Created by prasildas
 */
sealed class Screen(val route: String) {
    object Home : Screen("character_list")
    object Details : Screen("character_detail/{characterId}") {
        fun createRoute(characterId: String) = "character_detail/$characterId"
    }
}
