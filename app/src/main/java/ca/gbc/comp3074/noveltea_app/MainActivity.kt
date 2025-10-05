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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavHost()
                    }
                }
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
                       Column() {
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
                   )
                   // Search text box - not interactable as of now (placeholder)
                   Box(
                       modifier = Modifier
                           .weight(1f)
                           .padding(horizontal = 24.dp)
                           .background(Color.White)
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

@Composable
fun BookDetailScreen(navController: NavHostController, bookId: Int, books: List<Book>) {

}

@Composable
fun AppNavHost(){
    val navController = rememberNavController()
    val books: List<Book> = MockDataset.getBooks()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { BookGridHomeScreen(navController = navController, books) }
        composable("detail/{bookId}") { backstackEntry ->
            val bookId = backstackEntry.arguments?.getString("bookId")?.toIntOrNull() ?: 0
            BookDetailScreen(navController = navController, bookId = bookId, books = books)
        }
    }
}