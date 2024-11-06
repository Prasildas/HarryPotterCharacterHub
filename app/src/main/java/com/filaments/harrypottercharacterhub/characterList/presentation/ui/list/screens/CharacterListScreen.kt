package com.filaments.harrypottercharacterhub.characterList.presentation.ui.list.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val characterState by viewModel.characterListState.collectAsStateWithLifecycle()

    // Show loading indicator when loading data
    if (characterState.isLoading) {
        LoadingIndicator()
    } else {
        // Check for character list availability
        when {
            characterState.characterList.isEmpty() -> {
                EmptyStateView(onRetry = { viewModel.refreshCharacterList() })
            }
            else -> {
                SearchableCharacterList(
                    characters = characterState.characterList,
                    onCharacterClick = onCharacterClick
                )
            }
        }
    }

    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()
    toastMessage?.let {
        Toast.makeText(LocalContext.current, it, Toast.LENGTH_SHORT).show()
        viewModel.clearToastMessage()
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun EmptyStateView(onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.no_characters_found),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            RetryIcon(onRetry = onRetry)
        }
    }
}

@Composable
fun RetryIcon(onRetry: () -> Unit) {
    IconButton(onClick = { onRetry() }) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = stringResource(id = R.string.retry),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

