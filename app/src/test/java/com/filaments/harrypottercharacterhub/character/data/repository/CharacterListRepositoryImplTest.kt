package com.filaments.harrypottercharacterhub.character.data.repository

import app.cash.turbine.test
import com.filaments.harrypottercharacterhub.character.data.local.dao.CharacterDao
import com.filaments.harrypottercharacterhub.character.data.local.entities.CharacterEntity
import com.filaments.harrypottercharacterhub.character.data.mappers.toCharacter
import com.filaments.harrypottercharacterhub.character.data.mappers.toCharacterEntity
import com.filaments.harrypottercharacterhub.character.data.remote.model.CharacterDto
import com.filaments.harrypottercharacterhub.core.api.CustomAppException
import com.filaments.harrypottercharacterhub.core.api.HarryPotterApiService
import com.filaments.harrypottercharacterhub.core.api.Resource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Created by prasildas
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CharacterListRepositoryImplTest {

    private lateinit var repository: CharacterListRepositoryImpl
    private val mockApiService = mockk<HarryPotterApiService>()
    private val mockCharacterDao = mockk<CharacterDao>()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = CharacterListRepositoryImpl(mockApiService, mockCharacterDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when local data is available and forceFetchRemote is false, emit Success with local data`() = runTest {
        val localCharacters = listOf(
            CharacterEntity(
                id = "1",
                name = "Harry Potter",
                actor = "Daniel Radcliffe",
                house = "Gryffindor",
                dateOfBirth = "31-07-1980",
                alive = true,
                species = "",
                image = ""
            )
        )
        coEvery { mockCharacterDao.getAllCharacters() } returns localCharacters

        val flow = repository.getCharacterList(forceFetchRemote = false)

        flow.test {
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val successState = awaitItem()
            assertTrue(successState is Resource.Success)
            assertEquals(
                localCharacters.map { it.toCharacter() },
                (successState as Resource.Success).data
            )

            awaitComplete()
        }
    }

    @Test
    fun `when local data is empty or forceFetchRemote is true, fetch data from remote and save it`() = runTest {
        coEvery { mockCharacterDao.getAllCharacters() } returns emptyList()

        val remoteCharacters = listOf(
            CharacterDto(
                id = "1",
                name = "Harry Potter",
                actor = "Daniel Radcliffe",
                house = "Gryffindor",
                dateOfBirth = "31-07-1980",
                alive = true,
                species = "",
                image = ""
            )
        )
        coEvery { mockApiService.getCharacters() } returns remoteCharacters
        coEvery { mockCharacterDao.upsertCharactersList(any()) } just Runs

        val flow = repository.getCharacterList(forceFetchRemote = true)

        flow.test {
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val successState = awaitItem()
            assertTrue(successState is Resource.Success)
            assertEquals(
                remoteCharacters.map { it.toCharacterEntity().toCharacter() },
                (successState as Resource.Success).data
            )

            coVerify { mockCharacterDao.upsertCharactersList(any()) }

            awaitComplete()
        }
    }

    @Test
    fun `when local data is empty and API call fails with IOError, emit IOError`() = runTest {
        coEvery { mockCharacterDao.getAllCharacters() } returns emptyList()
        coEvery { mockApiService.getCharacters() } throws CustomAppException.IOError("Network error, please check your connection.", code = null)

        val flow = repository.getCharacterList(forceFetchRemote = true)

        flow.test {
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val errorState = awaitItem()
            assertTrue(errorState is Resource.Error)
            assertEquals("Network error, please check your connection.", (errorState as Resource.Error).message)

            awaitComplete()
        }
    }

    @Test
    fun `when API call fails with ServerError, emit ServerError`() = runTest {
        coEvery { mockCharacterDao.getAllCharacters() } returns emptyList()
        coEvery { mockApiService.getCharacters() } throws CustomAppException.ServerError("Server error, please try again later.")

        val flow = repository.getCharacterList(forceFetchRemote = true)

        flow.test {
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val errorState = awaitItem()
            assertTrue(errorState is Resource.Error)
            assertEquals("Server error, please try again later.", (errorState as Resource.Error).message)

            awaitComplete()
        }
    }

    @Test
    fun `when API call fails with UnknownError, emit UnknownError`() = runTest {
        coEvery { mockCharacterDao.getAllCharacters() } returns emptyList()
        coEvery { mockApiService.getCharacters() } throws CustomAppException.UnknownError("Unexpected error occurred.")

        val flow = repository.getCharacterList(forceFetchRemote = true)

        flow.test {
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val errorState = awaitItem()
            assertTrue(errorState is Resource.Error)
            assertEquals("Unexpected error occurred.", (errorState as Resource.Error).message)

            awaitComplete()
        }
    }
}
