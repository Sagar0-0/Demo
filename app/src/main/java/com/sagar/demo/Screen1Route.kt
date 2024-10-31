package com.sagar.demo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.android.billingclient.api.BillingClient.BillingResponseCode
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object Screen1Route

@Composable
fun Screen1() {
    var isPurchaseDone by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val helper = remember {
        BillingClientHelper(
            context = context,
            purchasesUpdatedListener = { result, purchases ->
                if (result.responseCode == BillingResponseCode.OK && purchases != null) {
                    isPurchaseDone = true
                }
            }
        )
    }

    DisposableEffect(context) {
        helper.startConnection()
        onDispose {
            helper.endConnection()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isPurchaseDone) {
            Text("Purchase Done")
        } else {
            Button(
                onClick = {
                    scope.launch {
                        helper.processPurchase()
                    }
                }
            ) {
                Text(text = "Show bill")
            }
        }
    }
}