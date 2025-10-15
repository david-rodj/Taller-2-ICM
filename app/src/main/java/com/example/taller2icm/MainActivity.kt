package com.example.taller2icm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taller2icm.ui.MainScreen
import com.example.taller2icm.ui.MapScreen
import com.example.taller2icm.ui.MediaScreen
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar osmdroid
        Configuration.getInstance().userAgentValue = packageName

        setContent {
            MaterialTheme {
                Surface {
                    Taller2ICMApp()
                }
            }
        }
    }
}

@Composable
fun Taller2ICMApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(
                onNavigateToMedia = { navController.navigate("media") },
                onNavigateToMap = { navController.navigate("map") }
            )
        }
        composable("media") {
            MediaScreen(onBack = { navController.popBackStack() })
        }
        composable("map") {
            MapScreen(onBack = { navController.popBackStack() })
        }
    }
}