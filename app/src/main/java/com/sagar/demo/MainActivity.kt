package com.sagar.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sagar.demo.screen.Screen1
import com.sagar.demo.screen.Screen1Route
import com.sagar.demo.screen.Screen2
import com.sagar.demo.screen.Screen2Route
import com.sagar.demo.ui.theme.DemoTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DemoTheme {
                val navController = rememberNavController()
                SharedTransitionLayout {
                    NavHost(
                        navController = navController,
                        startDestination = Screen1Route
                    ) {
                        composable<Screen1Route> {
                            Screen1(
                                animatedVisibilityScope = this
                            ) { route ->
                                navController.navigate(
                                    route
                                )
                            }
                        }

                        composable<Screen2Route> {
                            val data = it.toRoute<Screen2Route>()
                            Screen2(
                                animatedVisibilityScope = this,
                                data
                            ) {
                                navController.popBackStack()
                            }
                        }
                    }
                }

            }
        }
    }
}



