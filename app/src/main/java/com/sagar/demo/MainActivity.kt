package com.sagar.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sagar.demo.ui.theme.DemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ListDetailLayout(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ListDetailLayout(modifier: Modifier = Modifier) {
    val navigator = rememberListDetailPaneScaffoldNavigator<String>()
    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            MainPane {
                navigator.navigateTo(
                    pane = ListDetailPaneScaffoldRole.Detail,
                    content = it
                )
            }
        },
        detailPane = {
            val id = navigator.currentDestination?.content
            DetailPane(id) {
                navigator.navigateTo(
                    pane = ListDetailPaneScaffoldRole.Extra,
                    content = id
                )
            }
        },
        extraPane = {
            val id = navigator.currentDestination?.content
            if (id != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.tertiaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Profile Pane of UserID: $id")
                }
            }
        }
    )

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }
}

@Composable
fun MainPane(onClick: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(50) {
            Text(
                "UserId $it",
                modifier = Modifier
                    .fillParentMaxWidth()
                    .clickable {
                        onClick(it.toString())
                    }
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun DetailPane(id: String?, onClick: () -> Unit) {
    Crossfade(targetState = id, label = "") {
        if (it == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Cyan),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Select Any User")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Cyan),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Detail of UserID: $it")
                AssistChip(
                    onClick = onClick,
                    label = {
                        Text(text = "Profile of User")
                    }
                )
            }
        }
    }
}