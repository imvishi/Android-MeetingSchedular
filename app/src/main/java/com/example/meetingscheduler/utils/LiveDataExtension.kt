package com.example.meetingscheduler.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeNonNull(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, Observer<T> { t ->
        t?.let { observer.onChanged(t) }
    })
}

fun <T> LiveData<T>.observeOnceAndNonNull(observer: Observer<T>) {
    observeForever(object : Observer<T> {
        override fun onChanged(t: T?) {
            t?.let {
                observer.onChanged(t)
                removeObserver(this)
            }
        }
    })
}