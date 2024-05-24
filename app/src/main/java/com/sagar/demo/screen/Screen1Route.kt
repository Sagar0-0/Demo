package com.sagar.demo.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sagar.demo.R
import kotlinx.serialization.Serializable

fun NavGraphBuilder.screen1Route(onClick: (Int, String) -> Unit) {
    composable<Screen1Route> {
        Screen1 { res, name ->
            onClick(res, name)
        }
    }
}

@Serializable
object Screen1Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen1(onClick: (Int, String) -> Unit) {
    Scaffold(
        topBar = {
            MediumTopAppBar(title = { Text(text = "Some Chat App") })
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(15) {
                Row(
                    modifier = Modifier.fillMaxWidth().clickable {
                        onClick(
                            R.drawable.profile,
                            "User Name ${it + 1}"
                        )
                    },
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .size(60.dp),
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = null
                    )

                    Text(
                        text = "User Name ${it + 1}",
                        style = MaterialTheme.typography.titleLarge
                    )

                }
            }

        }
    }
}