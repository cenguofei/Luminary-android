package com.example.lunimary

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController

val LocalNavNavController = staticCompositionLocalOf<NavController> {
    error("CompositionLocal NavController not present")
}