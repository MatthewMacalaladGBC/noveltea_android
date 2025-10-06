@file:OptIn(ExperimentalMaterial3Api::class)

package ca.gbc.comp3074.noveltea_app

import android.os.Bundle
import android.security.identity.AccessControlProfile
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.TopAppBar
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
    // Scaffold composable to stack top bar on top of grid layout
    Scaffold(
       // Using compose App Bar setups
       topBar = {
           // Column to stack two items
           // - TopAppBar for the app title and tagline
           // - Row to hold profile image and search bar
           // - (default icon and static text box used as UI placeholders)
           Column {
               // Top row - Title + tagline
               TopAppBar(
                   colors = TopAppBarDefaults.topAppBarColors(
                       containerColor = MaterialTheme.colorScheme.primaryContainer,
                       titleContentColor = MaterialTheme.colorScheme.primary,
                   ),
                   title = {
                       // Column inside of title for stacking multiple text items (varying size and weight)
                       Column {
                           Text(text = "Noveltea", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                           Text(text = "By book lovers, for book lovers", fontSize = 16.sp)
                       }
                   }
               )
               Spacer(modifier = Modifier.height(20.dp))
               // Row below holding profile image and search bar (not functional, placeholder for UI mock-up)
               Row(
                   modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                   horizontalArrangement = Arrangement.SpaceBetween,
                   verticalAlignment = Alignment.CenterVertically
               ) {
                   // Default image icon (pulled from lab 3)
                   Image(
                       painter = painterResource(R.drawable.ic_launcher_foreground),
                       contentDescription = "Logo",
                       modifier = Modifier
                           .size(50.dp)
                           .clip(CircleShape)
                           .border(2.dp,
                               MaterialTheme.colorScheme.primary,
                               CircleShape
                           )
                           .clickable { navController.navigate("profile") }
                   )
                   // Search text box - not interactable as of now (placeholder)
                   Box(
                       modifier = Modifier
                           .weight(1f)
                           .padding(horizontal = 24.dp)
                           .background(Color.White)
                           .border(width = 1.dp, color = MaterialTheme.colorScheme.onBackground)
                   ) {
                       Text(text = "Search...",
                           color = MaterialTheme.colorScheme.secondary,
                           modifier = Modifier.padding(start = 8.dp)
                       )
                   }
               }
           }

       }
    ) { innerPadding ->
        // Grid holding book cover images and basic info
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2 set columns
            modifier = Modifier.wrapContentWidth().padding(innerPadding),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
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

        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2 set columns
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.height(200.dp)
                    ) {
                        Text(text = "Row for profile image and User Name")
                    }
                    Row(
                        modifier = Modifier.height(100.dp)
                    ) {
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

// Later on, maybe have different files for each composable?
// At the very least, have AppNavHost as it's own (Separation of Concerns)
@Composable
fun AppNavHost(){
    // Sets up the navController and gets list of books from the mock dataset
    val navController = rememberNavController()
    val books: List<Book> = MockDataset.getBooks()

    // Defaults to "home" screen, or BookGridHomeScreen Composable
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { BookGridHomeScreen(navController = navController, books) }
        composable("detail/{bookId}") { backstackEntry ->
            // Retrieves bookId based on the bookId passed when clicking on any book in the grid.
            // Pulls from the passed navigation route, and turns it into an int (with a default of 0 if not found)
            val bookId = backstackEntry.arguments?.getString("bookId")?.toIntOrNull() ?: 0
            BookDetailScreen(navController = navController, bookId = bookId, books = books)
        }
        composable("profile") { ProfileScreen(navController = navController, books) }
    }
}
