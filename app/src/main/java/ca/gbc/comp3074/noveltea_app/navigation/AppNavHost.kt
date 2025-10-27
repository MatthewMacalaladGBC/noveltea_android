package ca.gbc.comp3074.noveltea_app.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.gbc.comp3074.noveltea_app.ui.screens.BookGridHomeScreen
import ca.gbc.comp3074.noveltea_app.ui.screens.BookDetailScreen
import ca.gbc.comp3074.noveltea_app.ui.screens.BookViewModel
import ca.gbc.comp3074.noveltea_app.ui.screens.ProfileScreen

@Composable
fun AppNavHost(){
    // Sets up the navController and gets list of books from the mock dataset
    val navController = rememberNavController()
//    val books: List<Book> = MockDataset.getBooks()

    val viewModel: BookViewModel = viewModel()

    // Defaults to "home" screen, or BookGridHomeScreen Composable
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { BookGridHomeScreen(navController = navController, viewModel) }
        composable("detail/{bookId}") { backstackEntry ->
            // Retrieves bookId based on the bookId passed when clicking on any book in the grid.
            // Pulls from the passed navigation route, and turns it into a string
            val bookId = backstackEntry.arguments?.getString("bookId") ?: ""
            BookDetailScreen(navController = navController, bookId = bookId, viewModel)
        }
        composable("profile") { ProfileScreen(navController = navController, viewModel) }
    }
}