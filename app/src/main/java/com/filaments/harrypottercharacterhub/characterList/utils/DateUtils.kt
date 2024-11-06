package com.filaments.harrypottercharacterhub.characterList.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by prasildas
 */
object DateUtils {
    fun formatDate(date: String): String {
        return try {
            val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val parsedDate = inputFormat.parse(date)
            outputFormat.format(parsedDate ?: Date())
        } catch (e: Exception) {
            "Unknown"
        }
    }
}