package com.filaments.harrypottercharacterhub.characterList.presentation.ui.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filaments.harrypottercharacterhub.characterList.domain.repository.CharacterListRepository
import com.filaments.harrypottercharacterhub.characterList.presentation.ui.list.state.CharacterListState
import com.filaments.harrypottercharacterhub.characterList.utils.Resource
import com.filaments.harrypottercharacterhub.characterList.utils.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by prasildas
 */
@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val characterListRepository: CharacterListRepository,
    private val stringProvider: StringProvider // Inject the StringProvider
) : ViewModel() {

    private val _characterListState = MutableStateFlow(CharacterListState())
    val characterListState: StateFlow<CharacterListState> = _characterListState.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage

    init {
        getCharacterList(forceFetchFromRemote = false)
    }

    private fun getCharacterList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            characterListRepository.getCharacterList(forceFetchFromRemote)
                .onStart {
                    _characterListState.update { it.copy(isLoading = true) }
                }
                .catch { exception ->
                    _characterListState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: stringProvider.unknownError()
                        )
                    }
                }
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _characterListState.update { it.copy(isLoading = true) }
                        }

                        is Resource.Success -> {
                            val newCharacters = result.data ?: emptyList()
                            if (_characterListState.value.characterList != newCharacters) {
                                _characterListState.update {
                                    it.copy(
                                        isLoading = false,
                                        characterList = newCharacters,
                                        errorMessage = null
                                    )
                                }
                            } else {
                                _characterListState.update {
                                    it.copy(isLoading = false)
                                }
                            }
                        }

                        is Resource.Error -> {
                            _characterListState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = result.message ?: stringProvider.unknownError()
                                )
                            }
                        }
                    }
                }
        }
    }

    fun refreshCharacterList() {
        getCharacterList(forceFetchFromRemote = true)
    }

    fun showToast(message: String) {
        _toastMessage.value = message
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }
}