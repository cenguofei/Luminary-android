package com.example.lunimary.design

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lunimary.design.theme.LunimaryTheme

@Composable
fun LunimaryToolbar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    between: @Composable RowScope.() -> Unit = {},
    end: @Composable BoxScope.() -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth().height(55.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            modifier = Modifier.padding(start = 12.dp),
            onClick = onBack
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
            Row(content = between)

            Box(
                modifier = Modifier.padding(end = 12.dp),
                contentAlignment = Alignment.Center,
                content = end
            )
        }
    }
}

@LightAndDarkPreview
@Composable
fun ToolbarPreview() {
    LunimaryTheme {
        LunimaryGradientBackground {
            LunimaryToolbar(
                onBack = { /*TODO*/ },
                between = {
                    Text(text = "Lunimary Blog", style = MaterialTheme.typography.headlineMedium)
                }
            ) {
                Icon(imageVector = Icons.Default.MoreVert
                    , contentDescription = null)
            }
        }
    }
}