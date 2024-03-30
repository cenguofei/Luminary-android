package com.example.lunimary.design

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController

val LocalNavNavController = staticCompositionLocalOf<NavController> {
    error("CompositionLocal NavController not present")
}