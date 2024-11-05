package com.sagar.demo

import android.Manifest.*
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkCallingOrSelfPermission
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sagar.demo.ui.theme.DemoTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        createSpeechRecognizer(this)
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
                                    list = listOf("a", "b", "c")
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

    override fun onDestroy() {
        super.onDestroy()
        mSpeechRecognizer?.destroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // audio permission granted
                Toast.makeText(this, "You can now use voice commands!", Toast.LENGTH_LONG).show()
                currentState = "Permission Granted"
            } else {
                // audio permission denied
                Toast.makeText(
                    this,
                    "Please provide microphone permission to use voice.",
                    Toast.LENGTH_LONG
                ).show()
                currentState = "Permission Denies"
            }
        }
    }
}

var mSpeechRecognizer: SpeechRecognizer? = null
var textForVoiceInput by mutableStateOf("")
var shouldEnd by mutableStateOf(true)
var currentState by mutableStateOf("Initial")

private fun handleSpeechBegin() {
    // start audio session
    mSpeechRecognizer?.startListening(createIntent())
}

private fun handleSpeechEnd() {
    // end audio session
    mSpeechRecognizer?.cancel()
}

private fun createSpeechRecognizer(context: Context) {
    mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    mSpeechRecognizer?.setRecognitionListener(object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle) {
            currentState = "Ready for speech"
        }
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray) {}

        override fun onEndOfSpeech() {
            currentState = "Ending..."
            shouldEnd = true
            CoroutineScope(Job()).launch {
                delay(600)
                if(shouldEnd){
                    handleSpeechEnd()
                    currentState = "Ended"
                }
            }
        }

        override fun onError(error: Int) {
            currentState = "Error..."
            handleSpeechEnd()
        }

        override fun onResults(results: Bundle) {
//            currentState = "Listening..."
//            // Called when recognition results are ready. This callback will be called when the
//            // audio session has been completed and user utterance has been parsed.
//
//            // This ArrayList contains the recognition results, if the list is non-empty,
//            // handle the user utterance
//            val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
//            if (matches != null && matches.size > 0) {
//                // The results are added in decreasing order of confidence to the list
//                val command = matches[0]
//                textForVoiceInput = command
//                shouldEnd = false
//            }
        }

        override fun onPartialResults(partialResults: Bundle) {
            currentState = "Partial listening..."
            // Called when partial recognition results are available, this callback will be
            // called each time a partial text result is ready while the user is speaking.
            val matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (matches != null && matches.size > 0) {
                // handle partial speech results
                val partialText = matches[0]
                textForVoiceInput = partialText
                shouldEnd = false
            }
        }

        override fun onEvent(eventType: Int, params: Bundle) {}
    })
}

private fun verifyAudioPermissions(context: Context) {
    if (checkCallingOrSelfPermission(
            context,
            permission.RECORD_AUDIO
        ) != PermissionChecker.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(permission.RECORD_AUDIO),
            123
        )
    } else {
        handleSpeechBegin()
    }
}

@Serializable
object Screen1Route

@Composable
fun Screen1(onClick: () -> Unit) {
    val context = LocalContext.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Text(textForVoiceInput)
            // Invoke the process from a chip
            AssistChip(
                onClick = {
                    verifyAudioPermissions(context)
                },
                label = {
                    Text("Start")
                },
            )
            Text(currentState)
        }
    }
}

private fun createIntent(): Intent {
    val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
    i.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-IN")
    return i
}

@Serializable
data class Screen2Route(
    val id: String,
    val name: String? = null,
    val list: List<String>
)

@Composable
fun Screen2(data: Screen2Route, onClick: () -> Unit) {
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