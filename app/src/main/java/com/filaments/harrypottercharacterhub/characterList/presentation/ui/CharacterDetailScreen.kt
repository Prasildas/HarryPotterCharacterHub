package com.filaments.harrypottercharacterhub.characterList.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.filaments.harrypottercharacterhub.R
import com.filaments.harrypottercharacterhub.characterList.common.DateUtils
import com.filaments.harrypottercharacterhub.characterList.domain.model.Character
import com.filaments.harrypottercharacterhub.characterList.presentation.CharacterListViewModel

/**
 * Created by prasildas
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(character: Character, viewModel: CharacterListViewModel) {
    val context = LocalContext.current

    // Observe the toast message state
    val toastMessage by viewModel.toastMessage.collectAsState()

    // Show the toast if there's a message
    toastMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        viewModel.clearToastMessage() // Clear the toast message after showing
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(character.name) })
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CharacterImage(character.image, character.name, viewModel)
                Spacer(modifier = Modifier.height(16.dp))
                CharacterDetails(character)
            }
        }
    )
}

@Composable
fun CharacterImage(imageUrl: String?, characterName: String, viewModel: CharacterListViewModel) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF123456), // Adjust the gradient colors as needed
                        Color(0xFF654321)
                    )
                )
            )
            .padding(8.dp)
    ) {
        val imageLoadErrorMessage = stringResource(id = R.string.image_load_error)

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl.takeIf { !it.isNullOrEmpty() } ?: R.drawable.placeholder)
                .crossfade(true)
                .error(R.drawable.placeholder) // Fallback if the image fails to load
                .placeholder(R.drawable.placeholder) // Placeholder while loading
                .build(),
            contentDescription = stringResource(
                id = R.string.character_image_description,
                characterName
            ),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .padding(8.dp),
            onError = {
                viewModel.showToast(imageLoadErrorMessage) // Trigger the toast message from the ViewModel
            }
        )
    }
}

@Composable
fun CharacterDetails(character: Character) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "${stringResource(id = R.string.actor_label)}: ${
                character.actor ?: stringResource(
                    id = R.string.unknown_actor
                )
            }",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "${stringResource(id = R.string.species_label)}: ${
                character.species ?: stringResource(
                    id = R.string.unknown
                )
            }",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "${stringResource(id = R.string.house_label)}: ${
                character.house ?: stringResource(
                    id = R.string.unknown
                )
            }",
            style = MaterialTheme.typography.bodyMedium
        )

        character.dateOfBirth?.let {
            Text(
                text = "${stringResource(id = R.string.date_of_birth_label)}: ${
                    DateUtils.formatDate(
                        it
                    )
                }",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }

        Text(
            text = "${stringResource(id = R.string.status_label)}: ${
                if (character.alive) stringResource(
                    id = R.string.alive
                ) else stringResource(id = R.string.deceased)
            }",
            style = MaterialTheme.typography.bodyMedium,
            color = if (character.alive) Color.Green else Color.Red,
            textAlign = TextAlign.Center
        )
    }
}
