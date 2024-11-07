package com.filaments.harrypottercharacterhub.utils

/**
 * Created by prasildas
 */
interface StringProvider {
    fun getString(resId: Int): String
    fun unknownError(): String
    fun networkError(): String
    fun serverError(): String
}
