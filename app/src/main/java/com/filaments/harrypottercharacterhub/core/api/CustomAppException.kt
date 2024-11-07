package com.filaments.harrypottercharacterhub.core.api

import java.io.IOException

/**
 * Created by prasildas
 */
sealed class CustomAppException(message: String, val code: Int? = null) : IOException(message) {
    class IOError(message: String, code: Int?) : CustomAppException(message, code)
    class ServerError(message: String) : CustomAppException(message)
    class UnknownError(message: String) : CustomAppException(message)
}
