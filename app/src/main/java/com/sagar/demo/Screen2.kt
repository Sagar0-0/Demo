package com.sagar.demo

import android.content.Context
import android.content.IntentFilter
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.phone.SmsRetriever
import kotlinx.serialization.Serializable

@Serializable
data object Screen2Route

@Composable
fun Screen2(onClick: () -> Unit) {
    val context = LocalContext.current
    var otp by remember { mutableStateOf<String?>(null) }

    DisposableEffect(context) {

        SmsRetriever.getClient(context).startSmsRetriever()

        val receiver = MySMSBroadcastReceiver().apply {
            initListener(
                object : MySMSBroadcastReceiver.Listener {
                    override fun onOtpReceived(value: String?) {
                        otp = value
                    }
                }
            )
        }
        val filter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.registerReceiver(receiver,filter, Context.RECEIVER_EXPORTED)
        }

        onDispose {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.unregisterReceiver(receiver)
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            if (otp == null) {
                CircularProgressIndicator()
                Text("Waiting for OTP")
            } else {
                Text(otp ?: "")
                Button(
                    onClick = {
                        onClick()
                    }
                ) {
                    Text(text = "Submit Otp")
                }
            }
        }
    }
}
