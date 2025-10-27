@file:OptIn(ExperimentalMaterial3Api::class)

package ca.gbc.comp3074.noveltea_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ca.gbc.comp3074.noveltea_app.R
import ca.gbc.comp3074.noveltea_app.data.local.NameStore
import ca.gbc.comp3074.noveltea_app.data.local.RatingStore
import ca.gbc.comp3074.noveltea_app.ui.components.StarDisplay
import coil.compose.AsyncImage

// Profile page screen to display profile info
// For the mock-up, will be populated with static dummy info
// Actual app will make use of dataset that holds user info
// Books listed in mock-up are just the ones from the mock dataset,
// but each user will hold their own list of books in actuality
@Composable
fun ProfileScreen(navController: NavHostController, viewModel: BookViewModel) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    val panelColor = Color(0xFF1F2A44)
    val name = (NameStore.getName(ctx) ?: "Guest").uppercase()

    val readingList by viewModel.readingList.collectAsState()
    val reviewedBooks by viewModel.reviewedBooks.collectAsState()
//    var readingList by remember { mutableStateOf(ReadingListStore.getBooks(ctx, books)) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("Noveltea", fontSize = 24.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // Header
            Surface(
                color = panelColor,
                tonalElevation = 2.dp,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        // simple safe avatar
                        androidx.compose.material3.OutlinedButton(
                            onClick = { /* no-op */ },
                            modifier = Modifier.size(64.dp),
                            contentPadding = PaddingValues(0.dp),
                            shape = CircleShape
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.face),
                                contentDescription = "Profile picture"
                            ) }

                        Spacer(Modifier.width(12.dp))
                        Text(name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("Followers 20", color = Color.White, modifier = Modifier.weight(1f))
                        Text("Following 35", color = Color.White, modifier = Modifier.weight(1f))
                        val reviewsCount = readingList.count { RatingStore.get(ctx, it.id) > 0f }
                        Text("Reviews $reviewsCount", color = Color.White, modifier = Modifier.weight(1f))
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {

                // --- Reading List Header ---
                item {
                    Spacer(Modifier.height(12.dp))
                    Text("Reading List", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.height(8.dp))
                }

                // --- Reading List Content ---
                if (readingList.isEmpty()) {
                    item {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Your reading list is empty.\nGo add some books from Home!",
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                } else {
                    item {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 1200.dp), // prevents infinite height
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(24.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(readingList, key = { it.id }) { b ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { navController.navigate("detail/${b.id}") }
                                ) {
                                    Surface(
                                        shadowElevation = 6.dp,
                                        shape = MaterialTheme.shapes.medium,
                                        modifier = Modifier.size(width = 150.dp, height = 220.dp)
                                    ) {
                                        AsyncImage(model = b.coverImgUrl, contentDescription = "Cover for ${b.title}")
                                    }
                                    Spacer(Modifier.height(8.dp))
                                    Text(b.title, fontWeight = FontWeight.Bold)
                                    Text(b.author, style = MaterialTheme.typography.bodySmall, fontStyle = FontStyle.Italic)
                                    val myRating = RatingStore.get(ctx, b.id)
                                    if (myRating > 0f) StarDisplay(value = myRating)
                                    TextButton(onClick = {
                                        viewModel.removeFromReadingList(b.id)
                                        android.widget.Toast.makeText(ctx, "Removed: ${b.title}", android.widget.Toast.LENGTH_SHORT).show()
                                    }) {
                                        Text("Remove")
                                    }
                                }
                            }
                        }
                    }
                }

                // --- Review List Header ---
                item {
                    Spacer(Modifier.height(24.dp))
                    Text("Review List", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.height(8.dp))
                }

                // --- Review List Content ---
                if (reviewedBooks.isEmpty()) {
                    item {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "You haven't reviewed any books yet.",
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                } else {
                    item {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 1200.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(24.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(reviewedBooks, key = { it.id }) { b ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { navController.navigate("detail/${b.id}") }
                                ) {
                                    Surface(
                                        shadowElevation = 6.dp,
                                        shape = MaterialTheme.shapes.medium,
                                        modifier = Modifier.size(width = 150.dp, height = 220.dp)
                                    ) {
                                        AsyncImage(model = b.coverImgUrl, contentDescription = "Cover for ${b.title}")
                                    }
                                    Spacer(Modifier.height(8.dp))
                                    Text(b.title, fontWeight = FontWeight.Bold)
                                    Text(b.author, style = MaterialTheme.typography.bodySmall, fontStyle = FontStyle.Italic)
                                    val myRating = RatingStore.get(ctx, b.id)
                                    if (myRating > 0f) StarDisplay(value = myRating)
                                    TextButton(onClick = {
                                        viewModel.removeFromReviewList(b.id)
                                        android.widget.Toast.makeText(ctx, "Removed: ${b.title}", android.widget.Toast.LENGTH_SHORT).show()
                                    }) {
                                        Text("Remove")
                                    }
                                }
                            }
                        }
                    }
                }
            }


        }
    }
}