package com.filaments.harrypottercharacterhub.characterList.presentation

import app.cash.turbine.test
import com.filaments.harrypottercharacterhub.characterList.domain.model.Character
import com.filaments.harrypottercharacterhub.characterList.domain.repository.CharacterListRepository
import com.filaments.harrypottercharacterhub.characterList.presentation.ui.list.state.CharacterListState
import com.filaments.harrypottercharacterhub.characterList.presentation.ui.list.viewmodel.CharacterListViewModel
import com.filaments.harrypottercharacterhub.characterList.utils.Resource
import com.filaments.harrypottercharacterhub.characterList.utils.StringProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
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

        // Default mock setup with delay between loading and success
        coEvery { repository.getCharacterList(any()) } returns flow {
            emit(Resource.Loading())
            delay(50)  // Introduce a small delay
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
            // Capture initial loading state
            val loadingState = awaitItem()
            assertEquals(CharacterListState(isLoading = true), loadingState)

            // Capture success state with character data
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

        // Override the repository to emit an error with a delay
        coEvery { repository.getCharacterList(any()) } returns flow {
            emit(Resource.Loading())
            delay(50)  // Delay to allow state observation
            emit(Resource.Error(errorMessage))
        }

        // Reinitialize ViewModel with updated mock behavior
        viewModel = CharacterListViewModel(repository, mockStringProvider)

        viewModel.characterListState.test {
            // Capture initial loading state
            val loadingState = awaitItem()
            assertEquals(CharacterListState(isLoading = true), loadingState)

            // Capture error state with error message
            val errorState = awaitItem()
            assertEquals(
                CharacterListState(isLoading = false, errorMessage = errorMessage),
                errorState
            )

            cancelAndConsumeRemainingEvents()
        }
    }
}