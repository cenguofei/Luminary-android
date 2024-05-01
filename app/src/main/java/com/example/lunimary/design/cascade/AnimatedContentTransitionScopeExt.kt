package com.example.lunimary.design.cascade

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.togetherWith

fun <T> AnimatedContentTransitionScope<T>.animateToPrevious(): ContentTransform {
    return slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right) togetherWith
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
}

@ExperimentalAnimationApi
fun <T> AnimatedContentTransitionScope<T>.animateToNext(): ContentTransform {
    return slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) togetherWith
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
}