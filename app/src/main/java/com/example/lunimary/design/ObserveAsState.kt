package com.example.lunimary.design

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

@Composable
fun <T> LiveData<T>.myObserveAsState(onChange: (T) -> Unit): State<T?> = myObserveAsState(value, onChange)

@Composable
fun <R, T : R> LiveData<T>.myObserveAsState(initial: R, onChange: (T) -> Unit): State<R> {
    val lifecycleOwner = LocalLifecycleOwner.current
    val state = remember {
        @Suppress("UNCHECKED_CAST") /* Initialized values of a LiveData<T> must be a T */
        (mutableStateOf(if (isInitialized) value as T else initial))
    }
    DisposableEffect(this, lifecycleOwner) {
        val observer = Observer<T> {
            state.value = it
            onChange(it)
        }
        observe(lifecycleOwner, observer)
        onDispose { removeObserver(observer) }
    }
    return state
}