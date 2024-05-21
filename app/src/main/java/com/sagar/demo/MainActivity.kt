package com.sagar.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sagar.demo.ui.theme.DemoTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DemoTheme {
                val navController = rememberNavController()
                NavHost(
                    modifier = Modifier.semantics {
                        testTagsAsResourceId = true
                    },
                    navController = navController,
                    startDestination = Screen1Route
                ) {
                    composable<Screen1Route> {
                        Screen1 {
                            navController.navigate(Screen2Route(it))
                        }
                    }

                    composable<Screen2Route> {
                        val data = it.toRoute<Screen2Route>()
                        Screen2(
                            data
                        )
                    }
                }
            }
        }
    }
}

@Serializable
object Screen1Route

@Composable
fun Screen1(onClick: (String) -> Unit) {
    val items = List(20) { "Item ${it + 1}" }
    LazyColumn(
        Modifier
            .fillMaxSize()
            .testTag("items_list"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(items) { item ->
            Text(
                text = item,
                modifier = Modifier.testTag("item")
                    .clickable {
                        onClick(item)
                    }
                    .padding(16.dp)
            )
        }
    }
}

@Serializable
data class Screen2Route(
    val text: String,
)

@Composable
fun Screen2(data: Screen2Route) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Text(text = data.text)
        }

    }
}