package com.sagar.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sagar.demo.ui.theme.DemoTheme
import kotlinx.serialization.Serializable

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
                    composable<Screen1Route> {
                        Screen1 {
                            navController.navigate(
                                Screen2Route(
                                    "123//",
                                    name = "Sagar//",
                                    list = listOf("a","b","c")
                                )
                            )
                        }
                    }

                    composable<Screen2Route> {
                        val data = it.toRoute<Screen2Route>()
                        Screen2(
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

@Serializable
object Screen1Route
@Composable
fun Screen1(onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = onClick
        ) {
            Text(text = "Navigate to Screen 2")
        }
    }
}

@Serializable
data class Screen2Route(
    val id: String,
    val name: String? = null,
    val list : List<String>
)

@Composable
fun Screen2(data: Screen2Route,onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Text(text = data.id)
            Text(text = data.name ?: "")
            data.list.forEach {
                Text(text = it)
            }
            Button(
                onClick = onClick
            ) {
                Text(text = "Navigate back")
            }
        }

    }
}