package ca.gbc.comp3074.noveltea_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList // Import needed for the list
import androidx.navigation.NavHostController // Import needed for the controller type
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.gbc.comp3074.noveltea_app.model.Book
import ca.gbc.comp3074.noveltea_app.data.MockDataset
import ca.gbc.comp3074.noveltea_app.ui.screens.BookGridHomeScreen
import ca.gbc.comp3074.noveltea_app.ui.screens.BookDetailScreen
import ca.gbc.comp3074.noveltea_app.ui.screens.ProfileScreen
import ca.gbc.comp3074.noveltea_app.ui.screens.LibraryScreen

@Composable
fun AppNavHost(
    // 1. Allow passing the existing navController (optional but good practice)
    navController: NavHostController = rememberNavController(),
    // 2. Receive the shared state list from MainActivity
    savedBooks: SnapshotStateList<Book>
) {
    // Gets list of all available books from the mock dataset
    val allBooks: List<Book> = MockDataset.getBooks()

    NavHost(navController = navController, startDestination = "home") {

        // HOME
        composable("home") {
            BookGridHomeScreen(navController = navController, books = allBooks)
        }

        // DETAIL
        composable("detail/{bookId}") { backstackEntry ->
            // 1. Parse the ID
            val bookId = backstackEntry.arguments?.getString("bookId")?.toIntOrNull() ?: 0

            // 2. Pass data and the "Add" callback to the Detail Screen
            BookDetailScreen(
                navController = navController,
                bookId = bookId,
                books = allBooks,
                onAddToLibrary = { bookToAdd ->
                    // Check if book is already saved to avoid duplicates
                    // (Assuming Book data class has a unique 'id' or correct equals() method)
                    val alreadySaved = savedBooks.any { it.id == bookToAdd.id }

                    if (!alreadySaved) {
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
                savedBooks = savedBooks // Pass the list to be displayed
            )
        }
    }
}
