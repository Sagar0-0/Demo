package com.sagar.demo

interface CustomObservable<T> {
    var data: T
    fun addObserver(observer: CustomObserver<T>)
    fun removeObserver(observer: CustomObserver<T>)
}

class CustomObservableImpl<T>(initialData: T) : CustomObservable<T> {
    private val observers: MutableList<CustomObserver<T>> = mutableListOf()

    override fun addObserver(observer: CustomObserver<T>) {
        observers.add(observer)
    }

    override fun removeObserver(observer: CustomObserver<T>) {
        observers.remove(observer)
    }

    private var _data: T = initialData
    override var data: T
        get() = _data
        set(value) {
            if (_data != value) {
                _data = value
                observers.forEach {
                    it.update(value)
                }
                println("All observers updated!")
            }
        }

}