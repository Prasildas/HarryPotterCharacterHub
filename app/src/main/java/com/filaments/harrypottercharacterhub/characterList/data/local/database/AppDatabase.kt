package com.filaments.harrypottercharacterhub.characterList.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.filaments.harrypottercharacterhub.characterList.data.local.dao.CharacterDao
import com.filaments.harrypottercharacterhub.characterList.data.local.entities.CharacterEntity

/**
 * Created by prasildas
 */

@Database(
    entities = [CharacterEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val characterDao: CharacterDao
}