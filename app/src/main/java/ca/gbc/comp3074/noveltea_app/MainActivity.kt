@file:OptIn(ExperimentalMaterial3Api::class)

package ca.gbc.comp3074.noveltea_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.gbc.comp3074.noveltea_app.ui.theme.Noveltea_appTheme
import coil.compose.AsyncImage
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Noveltea_appTheme {
                AppNavHost()
            }
        }
    }
}

// Composable for Home Screen (book grid layout)
@Composable
fun BookGridHomeScreen(navController: NavHostController, books: List<Book>) {
    val ctx = androidx.compose.ui.platform.LocalContext.current

    // login state
    var name by remember { mutableStateOf(NameStore.getName(ctx)) }
    var showLogin by remember { mutableStateOf(false) }

    // search
    var query by remember { mutableStateOf("") }
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
            Column {
                // Row 1: title left, login right
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

                // Row 2: profile button (left) + search (right)
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
                    ) { Text("👤") }

                    Spacer(Modifier.width(8.dp))

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
                    Text(book.title, fontWeight = FontWeight.Bold)
                    Text(book.author, style = MaterialTheme.typography.bodySmall, fontStyle = FontStyle.Italic)
                }
            }
        }
    }
}


// book details
@Composable
fun BookDetailScreen(navController: NavHostController, bookId: Int, books: List<Book>) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    val book = books.find { it.id == bookId }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
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

        var rating by remember { mutableStateOf(RatingStore.get(ctx, book.id)) }

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
            item {
                Button(
                    onClick = {
                        val ids = ReadingListStore.getIds(ctx)
                        if (book.id in ids) {
                            android.widget.Toast.makeText(ctx, "Already in your list", android.widget.Toast.LENGTH_SHORT).show()
                        } else {
                            ReadingListStore.add(ctx, book.id)
                            android.widget.Toast.makeText(ctx, "Added to your list", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add to Reading List")
                }
            }
            item {
                Text("Your Rating", fontWeight = FontWeight.Bold)
                StarRating(value = rating, onChange = { rating = it })
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        RatingStore.set(ctx, book.id, rating)
                        android.widget.Toast.makeText(
                            ctx,
                            "Saved rating: ${"%.1f".format(rating)} ★",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Rating")
                }
            }

        }
    }
}


// Profile page screen to display profile info
// For the mock-up, will be populated with static dummy info
// Actual app will make use of dataset that holds user info
// Books listed in mock-up are just the ones from the mock dataset,
// but each user will hold their own list of books in actuality
@Composable
fun ProfileScreen(navController: NavHostController, books: List<Book>) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    val panelColor = Color(0xFF1F2A44)
    val name = (NameStore.getName(ctx) ?: "Guest").uppercase()


    var readingList by remember { mutableStateOf(ReadingListStore.getBooks(ctx, books)) }

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
                        ) { Text("👤") }

                        Spacer(Modifier.width(12.dp))
                        Text(name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("Followers 20", color = Color.White, modifier = Modifier.weight(1f))
                        Text("Following 35", color = Color.White, modifier = Modifier.weight(1f))
                        val reviewsCount = books.count { RatingStore.get(ctx, it.id) > 0f }
                        Text("Reviews $reviewsCount", color = Color.White, modifier = Modifier.weight(1f))
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Text("Lists", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))

            if (readingList.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "Your reading list is empty.\nGo add some books from Home!",
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
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

                            androidx.compose.material3.TextButton(onClick = {
                                ReadingListStore.remove(ctx, b.id)
                                readingList = ReadingListStore.getBooks(ctx, books)
                                android.widget.Toast.makeText(ctx, "Removed: ${b.title}", android.widget.Toast.LENGTH_SHORT).show()
                            }) { Text("Remove") }
                        }
                    }
                }
            }
        }
    }
}


// Later on, maybe have different files for each composable?
// At the very least, have AppNavHost as it's own (Separation of Concerns)
@Composable
fun AppNavHost(){
    // Sets up the navController and gets list of books from the mock dataset
    val navController = rememberNavController()
    val books: List<Book> = MockDataset.getBooks()

    // Defaults to "home" screen, or BookGridHomeScreen Composable
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { BookGridHomeScreen(navController = navController, books = books) }
        composable("detail/{bookId}") { backstackEntry ->
            // Retrieves bookId based on the bookId passed when clicking on any book in the grid.
            // Pulls from the passed navigation route, and turns it into an int (with a default of 0 if not found)
            val bookId = backstackEntry.arguments?.getString("bookId")?.toIntOrNull() ?: 0
            BookDetailScreen(navController = navController, bookId = bookId, books = books)
        }
        composable("profile") { ProfileScreen(navController = navController, books) }
    }
}
