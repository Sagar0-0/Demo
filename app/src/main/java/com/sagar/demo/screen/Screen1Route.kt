package com.sagar.demo.screen

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sagar.demo.R
import kotlinx.serialization.Serializable

@Serializable
object Screen1Route

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.Screen1(
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClick: (Screen2Route) -> Unit
) {
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onClick(
                                Screen2Route(
                                    R.drawable.profile,
                                    "User Name ${it + 1}",
                                    "MyImage $it",
                                    "MyText $it",
                                )
                            )
                        },
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(key = "MyImage $it"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .size(60.dp),
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = null
                    )

                    Text(
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(key = "MyText $it"),
                                animatedVisibilityScope = animatedVisibilityScope
                            ),
                        text = "User Name ${it + 1}",
                        style = MaterialTheme.typography.titleLarge
                    )

                }
            }

        }
    }
}