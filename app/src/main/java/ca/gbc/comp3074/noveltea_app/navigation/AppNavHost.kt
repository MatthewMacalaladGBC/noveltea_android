package ca.gbc.comp3074.noveltea_app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.gbc.comp3074.noveltea_app.model.Book
import ca.gbc.comp3074.noveltea_app.data.MockDataset
import ca.gbc.comp3074.noveltea_app.ui.screens.BookGridHomeScreen
import ca.gbc.comp3074.noveltea_app.ui.screens.BookDetailScreen
import ca.gbc.comp3074.noveltea_app.ui.screens.ProfileScreen
import ca.gbc.comp3074.noveltea_app.ui.screens.SearchScreen

@Composable
fun AppNavHost(){
    // Sets up the navController and gets list of books from the mock dataset
    val navController = rememberNavController()
    val books: List<Book> = MockDataset.getBooks()

    // Defaults to "home" screen, or BookGridHomeScreen Composable
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { 
            BookGridHomeScreen(
                navController = navController, 
                books = books,
                onSearchClick = { navController.navigate("search") }
            ) 
        }
        composable("detail/{bookId}") { backstackEntry ->
            // Retrieves bookId based on the bookId passed when clicking on any book in the grid.
            // Pulls from the passed navigation route, and turns it into an int (with a default of 0 if not found)
            val bookId = backstackEntry.arguments?.getString("bookId")?.toIntOrNull() ?: 0
            BookDetailScreen(navController = navController, bookId = bookId, books = books)
        }
composable("profile") { ProfileScreen(navController = navController, books) }
        composable("search") { 
            SearchScreen(
                navController = navController,
                books = books
            ) 
        }
    }
}