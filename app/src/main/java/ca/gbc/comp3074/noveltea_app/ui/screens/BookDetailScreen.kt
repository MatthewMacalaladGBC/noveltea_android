@file:OptIn(ExperimentalMaterial3Api::class)

package ca.gbc.comp3074.noveltea_app.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ca.gbc.comp3074.noveltea_app.model.Book
import coil.compose.AsyncImage

// Composable for Details page (book details)
@Composable
fun BookDetailScreen(navController: NavHostController, bookId: Int, books: List<Book>) {
    // Finds the specific book from the book list dataset, based on the provided id
    val book = books.find { it.id == bookId }

    // Sets up another scaffold to allow for a top bar
    // Displays a button to return to the home screen, as well as the app name
    Scaffold(
        topBar = {
            // Center aligned top bar instead of the default small top bar
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(text = "Noveltea", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                },
                // Back arrow icon to take you back to the Home screen
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back button, return to Home screen")
                    }
                }
            )
        }
    ) { innerPadding ->

        // If the book exists, then will display the details pulled from the data set
        book?.let {
            Column(
                modifier = Modifier.padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Cover image, centered and resized with padding
                Surface(
                    modifier = Modifier
                        .height(300.dp)
                        .width(200.dp)
                        .padding(top = 30.dp),
                    tonalElevation = 12.dp
                ) {
                    AsyncImage(
                        model = book.coverImgUrl,
                        contentDescription = "Cover image for ${book.title}"
                    )
                }
                Spacer(Modifier.height(15.dp))
                // Column to stack different text elements
                // Horizontal padding so that text is not full-screen
                Column(
                    modifier = Modifier.padding(horizontal = 25.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Stacked items: title, author, rating, description, all spaced out
                    Text(text = it.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text(text = "by ${it.author}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(8.dp))
                    Text(text = "★ ${it.rating}", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(16.dp))
                    Text(text = it.description, style = MaterialTheme.typography.bodyMedium)
                }

            }
        } ?: Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            // If the passed book ID cannot be found, will display a default message instead
            Text("Book not found.")
        }
    }
}