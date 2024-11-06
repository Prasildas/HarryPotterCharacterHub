package com.filaments.harrypottercharacterhub.characterList.data.repository

import app.cash.turbine.test
import com.filaments.harrypottercharacterhub.characterList.data.local.dao.CharacterDao
import com.filaments.harrypottercharacterhub.characterList.data.local.database.AppDatabase
import com.filaments.harrypottercharacterhub.characterList.data.local.entities.CharacterEntity
import com.filaments.harrypottercharacterhub.characterList.data.mappers.toCharacter
import com.filaments.harrypottercharacterhub.characterList.data.mappers.toCharacterEntity
import com.filaments.harrypottercharacterhub.characterList.data.remote.api.HarryPotterApiService
import com.filaments.harrypottercharacterhub.characterList.data.remote.model.CharacterDto
import com.filaments.harrypottercharacterhub.characterList.utils.Resource
import com.filaments.harrypottercharacterhub.characterList.utils.StringProvider
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
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
import java.io.IOException

/**
 * Created by prasildas
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CharacterListRepositoryImplTest {

    private lateinit var repository: CharacterListRepositoryImpl
    private val mockApiService = mockk<HarryPotterApiService>()
    private val mockDatabase = mockk<AppDatabase>()
    private val mockCharacterDao = mockk<CharacterDao>()
    private val mockStringProvider = mockk<StringProvider>()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { mockDatabase.characterDao } returns mockCharacterDao
        every { mockStringProvider.networkError() } returns "Network error, please check your connection."
        every { mockStringProvider.serverError() } returns "Server error, please try again later."
        every { mockStringProvider.unknownError() } returns "Unexpected error occurred."

        repository = CharacterListRepositoryImpl(mockApiService, mockDatabase, mockStringProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when local data is available and forceFetchRemote is false, emit Success with local data`() =
        runTest {
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
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `when local data is empty or forceFetchRemote is true, fetch data from remote and save it`() =
        runTest {
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
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `when local data is available but differs from remote data, update database and emit new data`() =
        runTest {
            // Arrange
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

            val remoteCharacters = listOf(
                CharacterDto(
                    id = "1",
                    name = "Harry Potter",
                    actor = "Daniel Radcliffe",
                    house = "Gryffindor",
                    dateOfBirth = "01-01-1990", // Changed date to simulate data difference
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
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `when API call fails with IOException, emit Error and stop loading`() = runTest {
        coEvery { mockCharacterDao.getAllCharacters() } returns emptyList()
        coEvery { mockApiService.getCharacters() } throws IOException()

        val flow = repository.getCharacterList(forceFetchRemote = true)

        flow.test {
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val errorState = awaitItem()
            assertTrue(errorState is Resource.Error)
            assertEquals(
                "Network error, please check your connection.",
                (errorState as Resource.Error).message
            )

            awaitComplete()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when API call fails with generic Exception, emit Error and stop loading`() = runTest {
        coEvery { mockCharacterDao.getAllCharacters() } returns emptyList()
        coEvery { mockApiService.getCharacters() } throws Exception("Unexpected error")

        val flow = repository.getCharacterList(forceFetchRemote = true)

        flow.test {
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val errorState = awaitItem()
            assertTrue(errorState is Resource.Error)
            assertEquals("Unexpected error occurred.", (errorState as Resource.Error).message)

            awaitComplete()
            cancelAndIgnoreRemainingEvents()
        }
    }
}