@file:OptIn(ExperimentalMaterial3Api::class)

package ca.gbc.comp3074.noveltea_app

// Add this import
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import ca.gbc.comp3074.noveltea_app.model.Book
import ca.gbc.comp3074.noveltea_app.navigation.AppNavHost
import ca.gbc.comp3074.noveltea_app.ui.theme.Noveltea_appTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Noveltea_appTheme {
                // 1. Create the state first
                val savedBooks = remember { mutableStateListOf<Book>() }

                // 2. Pass it to your navigation host
                // (You will need to update AppNavHost definition to accept this parameter)
                AppNavHost(savedBooks = savedBooks)
            }
        }
    }
}
