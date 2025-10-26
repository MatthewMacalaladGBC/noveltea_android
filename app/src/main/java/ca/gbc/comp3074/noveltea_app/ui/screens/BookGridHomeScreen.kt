@file:OptIn(ExperimentalMaterial3Api::class)

package ca.gbc.comp3074.noveltea_app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ca.gbc.comp3074.noveltea_app.model.Book
import ca.gbc.comp3074.noveltea_app.data.local.NameStore
import ca.gbc.comp3074.noveltea_app.ui.components.NameLogin
import ca.gbc.comp3074.noveltea_app.R
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource


// Composable for Home Screen (book grid layout)
@Composable
fun BookGridHomeScreen(navController: NavHostController, books: List<Book>) {
    val ctx = androidx.compose.ui.platform.LocalContext.current

    // user login state
    var name by remember { mutableStateOf(NameStore.getName(ctx)) }
    var showLogin by remember { mutableStateOf(false) }

    // search box
    var query by remember { mutableStateOf("") }
    // filter the book list
    val filtered = remember(query, books) {
        val q = query.trim().lowercase()
        if (q.isEmpty()) books
        else books.filter { it.title.lowercase().contains(q) || it.author.lowercase().contains(q) }
    }

    // Login
    if (showLogin) {
        NameLogin(
            onDismiss = { showLogin = false },
            onSubmit = { entered ->
                NameStore.setName(ctx, entered)
                name = entered
                showLogin = false
                android.widget.Toast.makeText(ctx, "Hello, $entered!", android.widget.Toast.LENGTH_SHORT).show()
            }
        )
    }


    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
            ) {
                // Row #1: title left, login right
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Noveltea", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text("By book lovers, for book lovers", style = MaterialTheme.typography.bodySmall)
                    }
                    //login or logout button
                    Button(onClick = {
                        if (name.isNullOrBlank()) {
                            showLogin = true
                        } else {
                            NameStore.clear(ctx)
                            name = null
                            android.widget.Toast.makeText(ctx, "Logged out", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text(if (name.isNullOrBlank()) "Login" else "Logout")
                    }
                }

                // Row #2: profile button (left) and search (right)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // avatar button
                    OutlinedButton(
                        onClick = { navController.navigate("profile") },
                        modifier = Modifier.size(40.dp),
                        contentPadding = PaddingValues(0.dp),
                        shape = CircleShape
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.face),
                            contentDescription = "Profile picture"
                    ) }

                    Spacer(Modifier.width(8.dp))

                    //search text area
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Search…") },
                        singleLine = true
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(filtered, key = { it.id }) { book ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("detail/${book.id}") }
                ) {
                    Surface(
                        shadowElevation = 6.dp,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.size(width = 150.dp, height = 220.dp)
                    ) {
                        coil.compose.AsyncImage(
                            model = book.coverImgUrl,
                            contentDescription = "Cover image for ${book.title}"
                        )
                    }
                    Spacer(Modifier.height(8.dp))

                    //Book title and author
                    Text(book.title, fontWeight = FontWeight.Bold)
                    Text(book.author, style = MaterialTheme.typography.bodySmall, fontStyle = FontStyle.Italic)
                }
            }
        }
    }
}