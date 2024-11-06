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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.filaments.harrypottercharacterhub.characterList.presentation.ui.list.nav.Screen
import com.filaments.harrypottercharacterhub.characterList.presentation.ui.list.screens.CharacterDetailScreen
import com.filaments.harrypottercharacterhub.characterList.presentation.ui.list.screens.CharacterListScreen
import com.filaments.harrypottercharacterhub.characterList.presentation.ui.list.viewmodel.CharacterListViewModel
import com.filaments.harrypottercharacterhub.characterList.presentation.ui.theme.HarryPotterCharacterHubTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HarryPotterCharacterHubTheme {
                SetBarColor(color = MaterialTheme.colorScheme.inverseOnSurface)
                // A surface container using the 'background' color from the theme
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
        val viewModel: CharacterListViewModel = hiltViewModel() // Retrieve ViewModel

        NavHost(navController = navController, startDestination = Screen.Home.route) {
            composable(Screen.Home.route) {
                CharacterListScreen(onCharacterClick = { characterId ->
                    navController.navigate(Screen.Details.createRoute(characterId))
                })
            }
            composable(
                route = Screen.Details.route,
                arguments = listOf(navArgument("characterId") { type = NavType.StringType })
            ) { backStackEntry ->
                val characterId = backStackEntry.arguments?.getString("characterId")

                // Collect the character list state
                val characterListState by viewModel.characterListState.collectAsStateWithLifecycle()

                // Find the character by ID
                val character = characterListState.characterList.find { it.id == characterId }

                if (character != null) {
                    // Pass the viewModel to CharacterDetailScreen
                    CharacterDetailScreen(character = character, viewModel = viewModel)
                } else {
                    Text(text = stringResource(id = R.string.character_not_found)) // Use a string resource for this message
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


