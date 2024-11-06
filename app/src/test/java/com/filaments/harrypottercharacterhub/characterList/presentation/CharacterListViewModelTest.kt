package com.filaments.harrypottercharacterhub.characterList.presentation

import app.cash.turbine.test
import com.filaments.harrypottercharacterhub.characterList.data.mappers.toCharacter
import com.filaments.harrypottercharacterhub.characterList.data.remote.model.CharacterDto
import com.filaments.harrypottercharacterhub.characterList.domain.model.Character
import com.filaments.harrypottercharacterhub.characterList.domain.repository.CharacterListRepository
import com.filaments.harrypottercharacterhub.characterList.presentation.ui.state.CharacterListState
import com.filaments.harrypottercharacterhub.characterList.presentation.ui.viewmodel.CharacterListViewModel
import com.filaments.harrypottercharacterhub.characterList.utils.Resource
import com.filaments.harrypottercharacterhub.characterList.utils.StringProvider
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by prasildas
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CharacterListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val repository = mockk<CharacterListRepository>()
    private lateinit var viewModel: CharacterListViewModel
    private val mockStringProvider = mockk<StringProvider>()

    private val mockCharacters = listOf(
        Character(
            id = "1",
            name = "Harry Potter",
            actor = "Daniel Radcliffe",
            house = "Gryffindor",
            dateOfBirth = "31-07-1980",
            alive = true,
            species = "",
            image = ""
        ),
        Character(
            id = "2",
            name = "Hermione Granger",
            actor = "Emma Watson",
            house = "Gryffindor",
            dateOfBirth = "19-09-1979",
            alive = true,
            species = "",
            image = ""
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        coEvery { repository.getCharacterList(false) } returns flow {
            emit(Resource.Loading())
            delay(50)
            emit(Resource.Success(mockCharacters))
        }

        viewModel = CharacterListViewModel(repository, mockStringProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCharacterList emits loading and success states`() = runTest {
        viewModel.characterListState.test {
            val loadingState = awaitItem()
            assertEquals(CharacterListState(isLoading = true), loadingState)

            val successState = awaitItem()
            assertEquals(
                CharacterListState(isLoading = false, characterList = mockCharacters),
                successState
            )

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getCharacterList emits loading and error states`() = runTest {
        val errorMessage = "Network Error"

        coEvery { repository.getCharacterList(any()) } returns flow {
            emit(Resource.Loading())
            delay(50)
            emit(Resource.Error(errorMessage))
        }

        viewModel = CharacterListViewModel(repository, mockStringProvider)

        viewModel.characterListState.test {
            val loadingState = awaitItem()
            assertEquals(CharacterListState(isLoading = true), loadingState)

            val errorState = awaitItem()
            assertEquals(
                CharacterListState(isLoading = false, errorMessage = errorMessage),
                errorState
            )

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `repository returns updated data`() = runTest {
        val newCharacter = CharacterDto(
            id = "1",
            name = "Harry Potter",
            actor = "Daniel Radcliffe",
            house = "Gryffindor",
            dateOfBirth = "01-01-1991",
            alive = true,
            species = "",
            image = ""
        )

        coEvery { repository.getCharacterList(true) } returns flow {
            emit(Resource.Loading())
            emit(Resource.Success(listOf(newCharacter.toCharacter())))
        }

        val result = repository.getCharacterList(true).toList()

        assertTrue(result[0] is Resource.Loading)

        assertTrue(result[1] is Resource.Success)

        val successData = (result[1] as Resource.Success).data
        assertEquals(listOf(newCharacter.toCharacter()), successData)
    }
}