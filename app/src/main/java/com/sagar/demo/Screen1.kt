package com.sagar.demo

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest
import com.google.android.gms.auth.api.identity.Identity

@Composable
fun Screen1() {
    val context = LocalContext.current

    val keyboardController = LocalSoftwareKeyboardController.current

    var phoneNumber by rememberSaveable { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }

    val phoneNumberHintIntentResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result->
        try {
            phoneNumber = Identity.getSignInClient(context)
                .getPhoneNumberFromIntent(result.data)
        } catch (e: Exception) {
            keyboardController?.show()
            Toast.makeText(
                context,
                "Phone Number Hint Cancelled. Please enter manually",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    val getPhoneNumberHint = {
        val request = GetPhoneNumberHintIntentRequest.builder().build()
        Identity.getSignInClient(context)
            .getPhoneNumberHintIntent(request)
            .addOnSuccessListener { intent ->
                phoneNumberHintIntentResultLauncher.launch(
                    IntentSenderRequest.Builder(intent).build()
                )
            }
            .addOnFailureListener {
                keyboardController?.show()
                Toast.makeText(
                    context,
                    "No Phone numbers found. Please enter manually",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
//            Button(
//                onClick = {
//                    getPhoneNumberHint()
//                }
//            ) {
//                Text(text = "Show Phone numbers")
//            }
//
//            Text(phoneNumber)

            TextField(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            keyboardController?.hide()
                            getPhoneNumberHint()
                        }
                    },
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = it
                },
                interactionSource = interactionSource
            )
        }

    }
}