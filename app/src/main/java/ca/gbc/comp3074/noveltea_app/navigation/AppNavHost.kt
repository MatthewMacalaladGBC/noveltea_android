package ca.gbc.comp3074.noveltea_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.gbc.comp3074.noveltea_app.model.Book
import ca.gbc.comp3074.noveltea_app.data.MockDataset
import ca.gbc.comp3074.noveltea_app.ui.screens.BookGridHomeScreen
import ca.gbc.comp3074.noveltea_app.ui.screens.BookDetailScreen
import ca.gbc.comp3074.noveltea_app.ui.screens.ProfileScreen
import ca.gbc.comp3074.noveltea_app.ui.screens.LibraryScreen
import ca.gbc.comp3074.noveltea_app.ui.screens.SearchScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    savedBooks: SnapshotStateList<Book>
) {
    val allBooks = MockDataset.getBooks()

    NavHost(navController = navController, startDestination = "home") {

        // HOME
        composable("home") {
            BookGridHomeScreen(
                navController = navController,
                books = allBooks,
                onSearchClick = { navController.navigate("search") }
            )
        }

        // DETAIL
        composable("detail/{bookId}") { backstackEntry ->
            val bookId = backstackEntry.arguments?.getString("bookId")?.toIntOrNull() ?: 0

            BookDetailScreen(
                navController = navController,
                bookId = bookId,
                books = allBooks,
                onAddToLibrary = { bookToAdd ->
                    if (savedBooks.none { it.id == bookToAdd.id }) {
                        savedBooks.add(bookToAdd)
                    }
                }
            )
        }

        // PROFILE
        composable("profile") {
            ProfileScreen(navController = navController, books = allBooks)
        }

        // LIBRARY
        composable("library") {
            LibraryScreen(
                navController = navController,
                savedBooks = savedBooks
            )
        }

        // SEARCH (from main branch)
        composable("search") {
            SearchScreen(
                navController = navController,
                books = allBooks
            )
        }
    }
}
