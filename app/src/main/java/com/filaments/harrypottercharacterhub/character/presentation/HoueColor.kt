package com.filaments.harrypottercharacterhub.character.presentation

import androidx.compose.ui.graphics.Color
import com.filaments.harrypottercharacterhub.core.theme.DefaultHouseColor
import com.filaments.harrypottercharacterhub.core.theme.GryffindorColor
import com.filaments.harrypottercharacterhub.core.theme.HufflepuffColor
import com.filaments.harrypottercharacterhub.core.theme.RavenclawColor
import com.filaments.harrypottercharacterhub.core.theme.SlytherinColor

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