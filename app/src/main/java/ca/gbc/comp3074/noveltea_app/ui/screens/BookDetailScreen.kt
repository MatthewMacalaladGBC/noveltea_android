@file:OptIn(ExperimentalMaterial3Api::class)

package ca.gbc.comp3074.noveltea_app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ca.gbc.comp3074.noveltea_app.data.local.RatingStore
import ca.gbc.comp3074.noveltea_app.data.local.ReadingListStore
import ca.gbc.comp3074.noveltea_app.model.Book
import ca.gbc.comp3074.noveltea_app.ui.components.StarRating
import coil.compose.AsyncImage

// Composable for Details page (book details)
@Composable
fun BookDetailScreen(
    navController: NavHostController,
    bookId: Int,
    books: List<Book>,
    onAddToLibrary: (Book) -> Unit // <--- 1. Add this parameter
) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    val book = books.find { it.id == bookId }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = Color.Unspecified
                ),
                title = { Text("Noveltea", fontSize = 30.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (book == null) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("Book not found.")
            }
            return@Scaffold
        }

        var rating by remember { mutableFloatStateOf(RatingStore.get(ctx, book.id)) }

        androidx.compose.foundation.lazy.LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // center the cover safely
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Surface(
                        tonalElevation = 12.dp,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .width(200.dp)
                            .height(300.dp)
                    ) {
                        AsyncImage(model = book.coverImgUrl, contentDescription = "Cover for ${book.title}")
                    }
                }
            }
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(book.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    Text("by ${book.author}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(6.dp))
                    Text("★ ${book.rating}", style = MaterialTheme.typography.titleLarge)
                }
            }
            item {
                Text(book.description, style = MaterialTheme.typography.bodyMedium)
            }

            // Existing Reading List Button (Keep this if you want both functionalities)
            item {
                Button(
                    onClick = {
                        val ids = ReadingListStore.getIds(ctx)
                        if (book.id in ids) {
                            Toast.makeText(ctx, "Already in your list", Toast.LENGTH_SHORT).show()
                        } else {
                            ReadingListStore.add(ctx, book.id)
                            Toast.makeText(ctx, "Added to your list", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add to Reading List (Local)")
                }
            }

            item {
                Text("Your Rating", fontWeight = FontWeight.Bold)
                StarRating(value = rating, onChange = { rating = it })
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        RatingStore.set(ctx, book.id, rating)
                        Toast.makeText(
                            ctx,
                            "Saved rating: ${"%.1f".format(rating)} ★",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Rating")
                }
            }

            // New "Add to Library" Button using the shared state
            item {
                Button(
                    onClick = {
                        // 2. Use the callback instead of accessing savedBooks directly
                        onAddToLibrary(book)
                        Toast.makeText(ctx, "Added to Library Tab", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                ) {
                    Text("Add to Library")
                }
            }
        }
    }
}
