package com.sagar.demo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val liveData = MyLiveData("")
        liveData.observe(this) { newValue->
            Toast.makeText(this, "Value changed to $newValue", Toast.LENGTH_SHORT).show()
        }

        setContent {
            Column(modifier = Modifier.padding(20.dp).statusBarsPadding()) {
                Button(
                    onClick = {
                        liveData.value += "A"
                    }
                ) {
                    Text("Increment")
                }
                Button(
                    onClick = {
                        liveData.value += "B"
                    }
                ) {
                    Text("Decrement")
                }
                Button(
                    onClick = {
                        liveData.observe(this@MainActivity) { newValue->
                            Toast.makeText(this@MainActivity, "Observer changed and value = $newValue", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Change Callback")
                }
            }
        }
    }
}