package com.sagar.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.sagar.demo.screen.Screen1Route
import com.sagar.demo.screen.Screen2Route
import com.sagar.demo.screen.screen1Route
import com.sagar.demo.screen.screen2Route
import com.sagar.demo.ui.theme.DemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DemoTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen1Route
                ) {
                    screen1Route { res, name ->
                        navController.navigate(
                            Screen2Route(
                                res, name
                            )
                        )
                    }

                    screen2Route {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}



