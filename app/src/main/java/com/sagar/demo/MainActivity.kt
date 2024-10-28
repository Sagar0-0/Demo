package com.sagar.demo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sagar.demo.ui.theme.DemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val list = AppSignatureHashHelper(this).appSignatures
        Log.d("TAG", "onCreate: $list")


        setContent {
            DemoTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen1Route) {
                    composable<Screen1Route> {
                        Screen1 {
                            navController.navigate(Screen2Route)
                        }
                    }

                    composable<Screen2Route> {
                        Screen2 {
                            navController.navigate(Screen3Route)
                        }
                    }

                    composable<Screen3Route> {
                        Screen3()
                    }
                }
            }
        }
    }
}