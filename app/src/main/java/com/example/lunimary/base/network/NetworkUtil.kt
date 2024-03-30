package com.example.lunimary.base.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.getSystemService

@SuppressLint("ObsoleteSdkInt")
@Suppress("DEPRECATION")
fun ConnectivityManager.isCurrentlyConnected() = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ->
        activeNetwork?.let(::getNetworkCapabilities)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    else -> activeNetworkInfo?.isConnected
} ?: false

fun Context.isCurrentlyConnected(): Boolean = getSystemService<ConnectivityManager>()?.isCurrentlyConnected() ?: false