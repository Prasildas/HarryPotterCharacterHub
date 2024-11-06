package com.filaments.harrypottercharacterhub.characterList.utils

import android.app.Application
import com.filaments.harrypottercharacterhub.R
import javax.inject.Inject

/**
 * Created by prasildas
 */
class ResourceStringProvider @Inject constructor(private val application: Application) : StringProvider {
    override fun getString(resId: Int): String {
        return application.getString(resId)
    }

    override fun unknownError(): String {
        return getString(R.string.unknown_error)
    }

    override fun networkError(): String {
        return getString(R.string.network_error)
    }

    override fun serverError(): String {
        return getString(R.string.server_error)
    }
}
