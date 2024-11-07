package com.filaments.harrypottercharacterhub.character.presentation.nav

import android.net.Uri
import com.filaments.harrypottercharacterhub.character.domain.model.Character
import com.google.gson.Gson

/**
 * Created by prasildas
 */
object CharacterNavigationHelper {
    fun encodeCharacter(character: Character): String {
        return Uri.encode(Gson().toJson(character))
    }

    fun decodeCharacter(characterJson: String?): Character? {
        return characterJson?.let {
            Gson().fromJson(it, Character::class.java)
        }
    }
}
