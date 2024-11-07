package com.filaments.harrypottercharacterhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.filaments.harrypottercharacterhub.character.presentation.nav.CharacterNavigationHelper
import com.filaments.harrypottercharacterhub.character.presentation.nav.CharactersScreen
import com.filaments.harrypottercharacterhub.character.presentation.ui.CharacterDetailScreen
import com.filaments.harrypottercharacterhub.character.presentation.ui.CharacterListScreen
import com.filaments.harrypottercharacterhub.core.theme.HarryPotterCharacterHubTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HarryPotterCharacterHubTheme {
                SetBarColor(color = MaterialTheme.colorScheme.inverseOnSurface)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }

    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = CharactersScreen.Home.route) {
            composable(CharactersScreen.Home.route) {
                CharacterListScreen(onCharacterClick = { character ->
                    val jsonCharacter = CharacterNavigationHelper.encodeCharacter(character)
                    navController.navigate(CharactersScreen.Details.createRoute(jsonCharacter))
                })
            }
            composable(
                route = CharactersScreen.Details.route,
                arguments = listOf(navArgument("characterJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val characterJson = backStackEntry.arguments?.getString("characterJson")
                val character = CharacterNavigationHelper.decodeCharacter(characterJson)
                if (character != null) {
                    CharacterDetailScreen(character = character)
                } else {
                    Text(text = stringResource(id = R.string.character_not_found))
                }
            }
        }
    }

    @Composable
    private fun SetBarColor(color: Color) {
        val systemUiController = rememberSystemUiController()
        LaunchedEffect(color) {
            systemUiController.setSystemBarsColor(color)
        }
    }
}



