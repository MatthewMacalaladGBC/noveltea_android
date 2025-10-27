package ca.gbc.comp3074.noveltea_app.ui.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.gbc.comp3074.noveltea_app.data.local.RatingStore
import ca.gbc.comp3074.noveltea_app.data.repository.BookRepository
import ca.gbc.comp3074.noveltea_app.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookViewModel : ViewModel() {
    private val repository = BookRepository()

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _selectedBook = MutableStateFlow<Book?>(null)
    val selectedBook: StateFlow<Book?> = _selectedBook

    private val _readingList = MutableStateFlow<List<Book>>(emptyList())
    val readingList: StateFlow<List<Book>> = _readingList

    private val _reviewedBooks = MutableStateFlow<List<Book>>(emptyList())
    val reviewedBooks: StateFlow<List<Book>> = _reviewedBooks

    // Loads random/trending books for home
    fun loadTrendingBooks() {
        viewModelScope.launch {
            _isLoading.value = true
            _books.value = repository.getTrendingBooks()
            _isLoading.value = false
        }
    }

    // Loads detailed data by workId for detail screen
    fun loadBookDetails(workId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _selectedBook.value = repository.getBookDetails(workId)
            _isLoading.value = false
        }
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                // If user clears search, reload trending books
                loadTrendingBooks()
                return@launch
            }

            _isLoading.value = true
            try {
                val results = repository.getBooksByQuery(query)
                _books.value = results.take(20) // limit number of pulled books
            } catch (e: Exception) {
                e.printStackTrace()
                _books.value = emptyList() // show empty screen if failed
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun addToReadingList(book: Book) {
        val updated = _readingList.value.toMutableList().apply {
            if (none { it.id == book.id }) add(book)
        }
        _readingList.value = updated
    }

    fun removeFromReadingList(id: String) {
        _readingList.value = _readingList.value.filterNot { it.id == id }
    }

    fun addReviewedBook(ctx: Context, book: Book, rating: Float) {
        RatingStore.set(ctx, book.id, rating)

        // Add to reviewedBooks state
        val updated = _reviewedBooks.value.toMutableList().apply {
            if (none { it.id == book.id }) add(book)
        }
        _reviewedBooks.value = updated
    }

    fun removeFromReviewList(id: String) {
        _reviewedBooks.value = _reviewedBooks.value.filterNot { it.id == id }
    }

}