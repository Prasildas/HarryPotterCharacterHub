package com.filaments.harrypottercharacterhub.characterList.presentation.ui.list.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.filaments.harrypottercharacterhub.R
import com.filaments.harrypottercharacterhub.characterList.domain.model.Character

/**
 * Created by prasildas
 */
@Composable
fun SearchableCharacterList(
    characters: List<Character>,
    onCharacterClick: (String) -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    // Filter characters based on the search text
    val filteredCharacters = characters.filter { character ->
        character.name.contains(searchText, ignoreCase = true) || character.actor.contains(
            searchText,
            ignoreCase = true
        )
    }

    Column {
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text(stringResource(id = R.string.search_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        // LazyColumn for displaying filtered characters
        LazyColumn {
            items(filteredCharacters) { character ->
                CharacterRow(
                    character = character,
                    onClick = { onCharacterClick(character.id) } // Use ID instead of name
                )
            }
        }
    }
}