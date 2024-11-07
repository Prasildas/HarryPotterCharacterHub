package com.filaments.harrypottercharacterhub.core.di

import android.app.Application
import androidx.room.Room
import com.filaments.harrypottercharacterhub.BuildConfig
import com.filaments.harrypottercharacterhub.character.data.local.dao.CharacterDao
import com.filaments.harrypottercharacterhub.core.api.ErrorHandlingInterceptor
import com.filaments.harrypottercharacterhub.core.api.HarryPotterApiService
import com.filaments.harrypottercharacterhub.core.database.AppDatabase
import com.filaments.harrypottercharacterhub.utils.ResourceStringProvider
import com.filaments.harrypottercharacterhub.utils.StringProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by prasildas
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideStringProvider(application: Application): StringProvider {
        return ResourceStringProvider(application)
    }

    @Singleton
    @Provides
    fun providesErrorHandlingInterceptor(stringProvider: StringProvider): ErrorHandlingInterceptor {
        return ErrorHandlingInterceptor(stringProvider)
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(
        errorHandlingInterceptor: ErrorHandlingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(errorHandlingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun providesHarryPotterApiService(retrofit: Retrofit): HarryPotterApiService {
        return retrofit.create(HarryPotterApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesCharactersDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "harrypotter.db"
        ).build()
    }

    @Singleton
    @Provides
    fun providesCharacterDao(database: AppDatabase): CharacterDao {
        return database.characterDao
    }

}