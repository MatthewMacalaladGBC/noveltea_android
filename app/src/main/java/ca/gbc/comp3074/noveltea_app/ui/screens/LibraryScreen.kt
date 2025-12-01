@file:OptIn(ExperimentalMaterial3Api::class)

package ca.gbc.comp3074.noveltea_app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ca.gbc.comp3074.noveltea_app.model.Book
import coil.compose.AsyncImage

@Composable
fun LibraryScreen(
    navController: NavHostController,
    savedBooks: List<Book>
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Library") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(innerPadding),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(savedBooks) { book ->
                Column(
                    modifier = Modifier
                        .width(150.dp)
                        .clickable { navController.navigate("detail/${book.id}") },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier
                            .size(width = 150.dp, height = 225.dp)
                            .padding(bottom = 8.dp),
                        shadowElevation = 12.dp
                    ) {
                        AsyncImage(
                            model = book.coverImgUrl,
                            contentDescription = "Cover image for ${book.title}"
                        )
                    }
                    Text(text = book.title, fontWeight = FontWeight.Bold)
                    Text(text = book.author)
                }
            }
        }
    }
}
