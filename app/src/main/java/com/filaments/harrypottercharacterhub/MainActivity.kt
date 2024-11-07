package com.filaments.harrypottercharacterhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.filaments.harrypottercharacterhub.character.presentation.nav.Route
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

        NavHost(navController = navController, startDestination = Route.List) {
            composable<Route.List> {
                CharacterListScreen(onCharacterClick = { characterId ->
                    navController.navigate(Route.Details(characterID = characterId))
                })
            }
            composable<Route.Details> { backStackEntry ->
                val args = backStackEntry.toRoute<Route.Details>()
                CharacterDetailScreen(characterId = args.characterID)
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



