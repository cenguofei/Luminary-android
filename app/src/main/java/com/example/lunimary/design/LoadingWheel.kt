package com.example.lunimary.design

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LoadingWheel(modifier: Modifier = Modifier) {
    Surface(modifier = modifier) {
        val anim = rememberLottieAnimatable()
        val composition by rememberLottieComposition(LottieCompositionSpec.Asset("loading.json"))
        LaunchedEffect(composition) {
            anim.animate(
                composition,
                iterations = LottieConstants.IterateForever,
            )
        }
        LottieAnimation(anim.composition, { anim.progress }, Modifier.fillMaxSize())
    }
}