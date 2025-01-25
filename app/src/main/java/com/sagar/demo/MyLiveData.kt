package com.sagar.demo

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

class MyLiveData<T>(initialValue: T) {
    var value: T = initialValue
        set(newValue) {
            field = newValue
            notifyAllObservers()
        }

    private val observers: MutableMap<Observer<T>, MyLifecycleEventObserver> = mutableMapOf()

    fun observe(owner: LifecycleOwner, observer: Observer<T>) {
        observer.onChanged(value)
        val lifecycleObserver = MyLifecycleEventObserver(owner, observer)
        observers[observer] = lifecycleObserver
        owner.lifecycle.addObserver(lifecycleObserver)
    }

    private fun notifyAllObservers() {
        observers.forEach { entry ->
            if (entry.value.isActive()) {
                entry.key.onChanged(value)
            }
        }
    }

    private fun removeObserver(observer: Observer<T>) {
        observers.remove(observer)
    }

    inner class MyLifecycleEventObserver(
        private val owner: LifecycleOwner, private val observer: Observer<T>
    ) : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            val currentState = source.lifecycle.currentState
            if (currentState == Lifecycle.State.DESTROYED) {
                removeObserver(observer)
                removeLifecycleObserver(source)
            }
        }

        private fun removeLifecycleObserver(owner: LifecycleOwner) {
            owner.lifecycle.removeObserver(this)
        }

        fun isActive(): Boolean {
            return owner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
        }
    }
}