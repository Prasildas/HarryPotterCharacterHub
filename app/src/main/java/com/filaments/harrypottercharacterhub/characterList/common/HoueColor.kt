package com.filaments.harrypottercharacterhub.characterList.common

import androidx.compose.ui.graphics.Color
import com.filaments.harrypottercharacterhub.characterList.presentation.ui.theme.DefaultHouseColor
import com.filaments.harrypottercharacterhub.characterList.presentation.ui.theme.GryffindorColor
import com.filaments.harrypottercharacterhub.characterList.presentation.ui.theme.HufflepuffColor
import com.filaments.harrypottercharacterhub.characterList.presentation.ui.theme.RavenclawColor
import com.filaments.harrypottercharacterhub.characterList.presentation.ui.theme.SlytherinColor

/**
 * Created by prasildas
 */
fun getHouseColor(house: String?): Color {
    return when (house) {
        "Gryffindor" -> GryffindorColor
        "Slytherin" -> SlytherinColor
        "Ravenclaw" -> RavenclawColor
        "Hufflepuff" -> HufflepuffColor
        else -> DefaultHouseColor // Default or unknown house color
    }
}