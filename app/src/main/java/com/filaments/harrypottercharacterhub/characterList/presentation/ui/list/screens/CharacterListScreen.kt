package com.filaments.harrypottercharacterhub.characterList.presentation.ui.list.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.filaments.harrypottercharacterhub.R
import com.filaments.harrypottercharacterhub.characterList.presentation.ui.list.viewmodel.CharacterListViewModel

/**
 * Created by prasildas
 */
@Composable
fun CharacterListScreen(
    viewModel: CharacterListViewModel = hiltViewModel(),
    onCharacterClick: (String) -> Unit
) {
    val characterState by viewModel.characterListState.collectAsState() // Observe character list state

    // Show loading indicator when loading data
    if (characterState.isLoading) {
        LoadingIndicator()
    } else {
        // Check for character list availability
        when {
            characterState.characterList.isEmpty() -> {
                EmptyStateView()
            }

            else -> {
                // Display the list of characters
                SearchableCharacterList(
                    characters = characterState.characterList,
                    onCharacterClick = onCharacterClick
                )
            }
        }
    }

    // Show toast if available
    val toastMessage by viewModel.toastMessage.collectAsState()
    toastMessage?.let {
        Toast.makeText(LocalContext.current, it, Toast.LENGTH_SHORT).show()
        viewModel.clearToastMessage() // Clear message after showing
    }
}

// Composable for loading indicator
@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator() // Display a loading spinner
    }
}

// Composable for empty state view
@Composable
fun EmptyStateView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.no_characters_found), // Use a string resource for the message
            style = MaterialTheme.typography.titleMedium
        )
    }
}

