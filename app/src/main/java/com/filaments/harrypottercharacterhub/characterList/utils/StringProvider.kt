package com.filaments.harrypottercharacterhub.characterList.utils

/**
 * Created by prasildas
 */
interface StringProvider {
    fun getString(resId: Int): String
    fun unknownError(): String
    fun networkError(): String
    fun serverError(): String
}
