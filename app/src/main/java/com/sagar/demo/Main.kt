package com.sagar.demo

import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext
import kotlin.math.acos

fun main() {
    val observableData = CustomObservableImpl("Sagar")

    val observer1 = CustomObserverImpl("")
    val observer2 = CustomObserverImpl("")
    val observer3 = CustomObserverImpl("")

    observableData.addObserver(observer1)
    observableData.addObserver(observer2)
    observableData.addObserver(observer3)

    runBlocking {
        repeat(5){ newValue->
            observableData.data = "Index = $newValue"
            delay(1000)
        }
    }

}