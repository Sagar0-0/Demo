package com.sagar.demo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val liveData = MutableLiveData<List<Int>>()
        liveData.observe(this) {
            it.map {  }.reduce { acc,
                                 unit ->  }
        }
        GlobalScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

            }
            val flow = produceStateFlow()
            launch {
                flow.collect {
                    Log.d("TAG", "First collector, value = $it")
                }
            }
            delay(6000)
            launch {
                flow.collect {
                    Log.e("TAG", "Second collector, value = $it")
                }
            }
        }
    }
}

private fun produceFlow(): Flow<Int> {
    return flow {
        repeat(5) { i ->
            emit(i)
            delay(1000)
        }
    }
}

private fun produceStateFlow(): StateFlow<Int> {
    val stateFlow = MutableStateFlow(0)
    GlobalScope.launch {
        repeat(5) { i ->
            stateFlow.update { i }
            delay(1000)
        }
    }
    return stateFlow
}

private fun produceSharedFlow(): SharedFlow<Int> {
    val sharedFlow = MutableSharedFlow<Int>()
    GlobalScope.launch {
        repeat(5) { i ->
            sharedFlow.emit(i)
            delay(1000)
        }
    }
    return sharedFlow
}
