package com.example.splitwise_final

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.splitwise_final.ui.SplashScreen
import com.example.splitwise_final.ui.theme.Splitwise_finalTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Splitwise_finalTheme {
                var showSplash by remember { mutableStateOf(true) }

                // Use LaunchedEffect for splash delay
                LaunchedEffect(Unit) {
                    delay(2000) // 2-second splash delay
                    showSplash = false
                }

                if (showSplash) {
                    SplashScreen()
                } else {
                    HomeScreen() // After splash, show your main screen (replace later)
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    androidx.compose.material3.Text(
        text = "Welcome to SettleUp!",
        modifier = Modifier
    )
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    Splitwise_finalTheme {
        SplashScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    Splitwise_finalTheme {
        HomeScreen()
    }
}