package com.sagar.demo

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class MyLiveData<T>(initialValue: T) {
    var value: T = initialValue
        set(newValue) {
            field = newValue
            notifyAllObservers()
        }

    private val observers: MutableMap<MyLifecycleEventObserver, (T) -> Unit> = mutableMapOf()

    fun observe(owner: LifecycleOwner, observer: (T) -> Unit) {
        observer(value)
        val lifecycleObserver = MyLifecycleEventObserver(owner)
        owner.lifecycle.addObserver(lifecycleObserver)
        observers[lifecycleObserver] = observer
    }

    private fun notifyAllObservers() {
        observers.forEach { entry ->
            entry.value.invoke(value)
        }
    }

    private fun removeObserver(lifecycleObserver: MyLifecycleEventObserver) {
        observers.remove(lifecycleObserver)
        lifecycleObserver.removeLifecycleObserver()
    }

    inner class MyLifecycleEventObserver(private val owner: LifecycleOwner) : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            val currentState = owner.lifecycle.currentState
            if(currentState==Lifecycle.State.DESTROYED) {
                Log.e("MyLiveData", "onStateChanged: removing our oberver")
                removeObserver(this)
            }
        }

        fun removeLifecycleObserver() {
            owner.lifecycle.removeObserver(this)
        }
    }
}