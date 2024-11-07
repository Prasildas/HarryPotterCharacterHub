package com.filaments.harrypottercharacterhub.character.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.filaments.harrypottercharacterhub.R
import com.filaments.harrypottercharacterhub.character.domain.model.Character
import com.filaments.harrypottercharacterhub.character.presentation.viewmodel.CharacterListViewModel
import com.filaments.harrypottercharacterhub.utils.DateUtils

/**
 * Created by prasildas
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    characterId: String,
    viewModel: CharacterListViewModel = hiltViewModel()
) {
    val characterListState by viewModel.characterListState.collectAsStateWithLifecycle()
    val character = characterListState.characterList.find { it.id == characterId }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    character?.name ?: stringResource(id = R.string.character_not_found)
                )
            })
        },
        content = { paddingValues ->
            if (character != null) {
                CharacterDetailContent(character, paddingValues)

            } else {
                Text(
                    text = stringResource(id = R.string.character_not_found),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}

@Composable
fun CharacterDetailContent(character: Character, paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            CharacterImage(character.image, character.name)
            Spacer(modifier = Modifier.height(16.dp))
            CharacterDetails(character)
        }
    }
}

@Composable
fun CharacterImage(imageUrl: String?, characterName: String) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                )
            )
            .padding(8.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl.takeIf { !it.isNullOrEmpty() } ?: R.drawable.placeholder)
                .crossfade(true)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
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
        )
    }
}

@Composable
fun CharacterDetails(character: Character) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "${stringResource(id = R.string.actor_label)}: ${character.actor}",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Text(
            text = "${stringResource(id = R.string.species_label)}: ${
                character.species ?: stringResource(
                    id = R.string.unknown
                )
            }",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        character.house?.takeIf { it.isNotEmpty() }?.let {
            Text(
                text = "${stringResource(id = R.string.house_label)}: ${
                    character.house
                }",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }

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
