package com.plcoding.bookpedia.book.presentation.book_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.core.domain.onError
import com.plcoding.bookpedia.core.domain.onSuccess
import com.plcoding.bookpedia.core.presentation.toUiText
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookListViewModel(
    private val bookRepository: BookRepository
): ViewModel() {

    private val cachedBooks = emptyList<Book>()

    private val _state = MutableStateFlow(BookListState())
    private var searchJob: Job? = null
    private var observeFavoriteJob: Job? = null
    val state = _state
        .onStart {
            if(cachedBooks.isEmpty()) {
                observeSearchQuery()
            }
            observeFavoriteBooks()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: BookListAction) {
        when(action) {
            is BookListAction.OnBookClick -> {

            }
            is BookListAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(searchQuery = action.query)
                }
            }
            is BookListAction.OnTabSelected -> {
                _state.update {
                    it.copy(selectedTabIndex = action.index)
                }
            }
        }
    }

    private fun observeFavoriteBooks() {
        observeFavoriteJob?.cancel()
        observeFavoriteJob = bookRepository
            .getFavoriteBooks()
            .onEach { favoriteBooks ->
                _state.update { it.copy(
                    favoriteBooks = favoriteBooks
                ) }
            }
            .launchIn(viewModelScope)
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        state
            .map { it.searchQuery }  // Chỉ lấy ra search query
            .distinctUntilChanged() // Nếu các state khác đổi nhưng search query không đổi thì không xử lý
            .debounce(500L) // chỉ phát ra giá trị nếu không có giá trị mới sau 500ms
            .onEach { query ->
                when {
                    query.isBlank() -> {
                        _state. update { it.copy(
                            errorMessage = null,
                            searchResults = cachedBooks
                        ) }
                    }

                    query.length >= 2 -> {
                        searchJob?.cancel()
                        searchJob = searchBooks(query)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun searchBooks(query: String) = viewModelScope.launch {
            _state.update { it.copy(
                isLoading = true
            ) }

            bookRepository
                .searchBooks(query)
                .onSuccess { searchResults ->
                    _state.update { it.copy(
                        isLoading = false,
                        errorMessage = null,
                        searchResults = searchResults,

                    ) }
                }
                .onError { error ->
                    _state.update { it.copy(
                        searchResults = emptyList(),
                        isLoading = false,
                        errorMessage = error.toUiText()
                    ) }
                }
    }
}