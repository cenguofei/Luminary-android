package com.example.lunimary.design

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.lunimary.design.theme.LunimaryTheme

@Composable
fun LinearButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    colors: List<Color> = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary
    ),
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    height: Dp = 30.dp,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(16),
    text: String
) {
    val linearColors = colors.ifEmpty {
        listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primary)
    }
    Surface(
        modifier = modifier
            .background(
                brush = Brush.linearGradient(linearColors),
                shape = shape
            )
            .height(height),
        onClick = onClick,
        color = Color.Transparent,
        enabled = enabled,
        shape = shape
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                color = Color.White,
                style = textStyle,
            )
        }
    }
}

@Composable
fun SmallButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.textShape,
    horizontalPadding: Dp = 8.dp,
    text: String
) {
    val textStyle = MaterialTheme.typography.labelMedium
    Surface(
        modifier = modifier.height(23.dp),
        onClick = onClick,
        color = MaterialTheme.colorScheme.primary,
        enabled = enabled,
        shape = shape
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = horizontalPadding),
                color = Color.White,
                style = textStyle,
            )
        }
    }
}

@LightAndDarkPreview
@Composable
fun LinearButtonDarkPreview() {
    LunimaryTheme(
        dynamicColor = false
    ) {
        LinearButton(
            onClick = {},
            text = "Preview"
        )
    }
}
