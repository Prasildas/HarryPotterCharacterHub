package com.filaments.harrypottercharacterhub.characterList.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.filaments.harrypottercharacterhub.R
import com.filaments.harrypottercharacterhub.characterList.common.getHouseColor
import com.filaments.harrypottercharacterhub.characterList.domain.model.Character

/**
 * Created by prasildas
 */
@Composable
fun CharacterRow(character: Character, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // House color indicator
            HouseIndicator(house = character.house)

            Spacer(modifier = Modifier.width(8.dp))

            // Character details
            Column {
                Text(text = character.name, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = buildString {
                        append(stringResource(id = R.string.played_by))
                        append(": ")
                        append(character.actor)
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
                character.house?.let {
                    Text(
                        text = "${stringResource(id = R.string.house_label)}: ${
                            stringResource(
                                id = when (it) {
                                    "Gryffindor" -> R.string.gryffindor
                                    "Slytherin" -> R.string.slytherin
                                    "Ravenclaw" -> R.string.ravenclaw
                                    "Hufflepuff" -> R.string.hufflepuff
                                    else -> R.string.unknown_actor
                                }
                            )
                        }", style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun HouseIndicator(house: String?) {
    Box(
        modifier = Modifier
            .size(16.dp)
            .background(
                color = getHouseColor(house),
                shape = CircleShape
            )
    )
}

