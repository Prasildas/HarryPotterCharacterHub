package com.filaments.harrypottercharacterhub.characterList.di

import android.app.Application
import androidx.room.Room
import com.filaments.harrypottercharacterhub.BuildConfig
import com.filaments.harrypottercharacterhub.characterList.data.local.database.AppDatabase
import com.filaments.harrypottercharacterhub.characterList.data.remote.api.HarryPotterApiService
import com.filaments.harrypottercharacterhub.characterList.utils.ResourceStringProvider
import com.filaments.harrypottercharacterhub.characterList.utils.StringProvider
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

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Singleton
    @Provides
    fun providesHarryPotterApiService(): HarryPotterApiService {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .build()
            .create(HarryPotterApiService::class.java)
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

    @Provides
    @Singleton
    fun provideStringProvider(application: Application): StringProvider {
        return ResourceStringProvider(application)
    }
}