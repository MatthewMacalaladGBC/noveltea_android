@file:OptIn(ExperimentalMaterial3Api::class)

package ca.gbc.comp3074.noveltea_app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ca.gbc.comp3074.noveltea_app.model.Book
import coil.compose.AsyncImage

// Profile page screen to display profile info
// For the mock-up, will be populated with static dummy info
// Actual app will make use of dataset that holds user info
// Books listed in mock-up are just the ones from the mock dataset,
// but each user will hold their own list of books in actuality
@Composable
fun ProfileScreen(navController: NavHostController, books: List<Book>) {
    // Same Scaffold topBar as the Details screen (find a way to reduce redundancy later)
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
        // Same lazy grid as what was used, with a few extra items above the book grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2 set columns
            modifier = Modifier
                .fillMaxSize() // fillMaxSize instead of wrapContentWidth() to keep it bounded
                .padding(innerPadding),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Item that spans both grid columns
            // Keeps objects in line in a single column instead of the two we see for the book grid
            item(span = { GridItemSpan(2) }) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.height(200.dp)
                    ) {
                        // Need to fill with actual elements to reflect actual layout
                        // static profile image, user name, etc.
                        Text(text = "Row for profile image and User Name")
                    }
                    Row(
                        modifier = Modifier.height(100.dp)
                    ) {
                        // Same as above, need properly laid-out mock-up
                        Text(text = "Row for profile info - followers, following, reviews")
                    }

                }
            }

            // Passes list of book items to be used in the grid
            items(books) { book ->
                Column(
                    modifier = Modifier.width(150.dp)
                        // On click, uses navController to navigate to BookDetailScreen composable
                        // Passes id of the clicked book to display proper information
                        .clickable { navController.navigate("detail/${book.id}") },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier.size(width = 150.dp, height = 225.dp).padding(bottom = 8.dp),
                        shadowElevation = 12.dp
                    ) {
                        AsyncImage(
                            model = book.coverImgUrl,
                            contentDescription = "Cover image for ${book.title}"
                        )
                    }
                    Text(text = book.title, fontWeight = FontWeight.Bold)
                    Text(text = book.author, fontStyle = FontStyle.Italic)
                }
            }
        }
    }
}