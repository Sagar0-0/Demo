package com.sagar.demo

interface CustomObserver<T> {
    fun update(value: T)
}

class CustomObserverImpl<T>(private var data: T) : CustomObserver<T> {
    override fun update(value: T) {
        data = value
        println("Updated to $data")
    }

    fun printData() {
        println("The current data is $data")
    }
}