package com.example.lunimary.design.background

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.lunimary.design.LightAndDarkPreview
import com.example.lunimary.design.theme.LunimaryTheme
import kotlin.math.tan

@Composable
fun LunimaryBackground(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    content: @Composable () -> Unit,
) {
    val color = LocalBackgroundTheme.current.color
    val tonalElevation = LocalBackgroundTheme.current.tonalElevation
    Surface(
        color = if (color == Color.Unspecified) Color.Transparent else color,
        tonalElevation = if (tonalElevation == Dp.Unspecified) 0.dp else tonalElevation,
        modifier = modifier.fillMaxSize(),
        shape = shape
    ) {
        CompositionLocalProvider(LocalAbsoluteTonalElevation provides 0.dp) {
            content()
        }
    }
}

@Composable
fun LunimaryGradientBackground(
    modifier: Modifier = Modifier,
    gradientColors: GradientColors = LocalGradientColors.current,
    shape: Shape = RectangleShape,
    content: @Composable () -> Unit
) {
    val currentTopColor by rememberUpdatedState(gradientColors.top)
    val currentBottomColor by rememberUpdatedState(gradientColors.bottom)
    GradientBackground(
        modifier = modifier,
        gradientColors = gradientColors,
        shape = shape,
        currentBottomColor = currentBottomColor,
        currentTopColor = currentTopColor,
        content = content
    )
}

@Composable
private fun GradientBackground(
    modifier: Modifier = Modifier,
    gradientColors: GradientColors = LocalGradientColors.current,
    shape: Shape = RectangleShape,
    content: @Composable () -> Unit,
    currentBottomColor: Color,
    currentTopColor: Color
) {
    Surface(
        color = if (gradientColors.container == Color.Unspecified) {
            Color.Transparent
        } else {
            gradientColors.container
        },
        modifier = modifier.fillMaxSize(),
        shape = shape
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawWithCache {
                    val offset = size.height * tan(
                        Math
                            .toRadians(11.06)
                            .toFloat()
                    )
                    val start = Offset(size.width / 2 + offset / 2, 0f)
                    val end = Offset(size.width / 2 - offset / 2, size.height)

                    val topGradient = Brush.linearGradient(
                        0f to if (currentTopColor == Color.Unspecified) {
                            Color.Transparent
                        } else {
                            currentTopColor
                        },
                        0.724f to Color.Transparent,
                        start = start,
                        end = end
                    )
                    val bottomGradient = Brush.linearGradient(
                        0.2552f to Color.Transparent,
                        1f to if (currentBottomColor == Color.Unspecified) {
                            Color.Transparent
                        } else {
                            currentBottomColor
                        },
                        start = start,
                        end = end,
                    )

                    onDrawBehind {
                        drawRect(topGradient)
                        drawRect(bottomGradient)
                    }
                }
        ) { content() }
    }
}

@LightAndDarkPreview
@Composable
fun BackgroundPreview() {
    LunimaryTheme {
        LunimaryBackground(Modifier.size(100.dp), content = {})
    }
}

@LightAndDarkPreview
@Composable
fun GradientBackgroundPreview() {
    LunimaryTheme(dynamicColor = false) {
        LunimaryGradientBackground(Modifier.size(100.dp), content = {})
    }
}