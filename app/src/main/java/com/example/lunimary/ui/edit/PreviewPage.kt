package com.example.lunimary.ui.edit

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.lunimary.R
import com.example.lunimary.base.DarkThemeSetting
import com.example.lunimary.base.SettingMMKV
import com.example.lunimary.design.LinearButton
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun PreviewPage(viewModel: EditViewModel, onEditClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)) {
            Text(
                text = viewModel.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
            val systemDarkMode = isSystemInDarkTheme()
            MarkdownText(
                modifier = Modifier,
                markdown = viewModel.body.trimIndent(),
                linkColor = when  {
                    SettingMMKV.userHasSetTheme -> {
                        if (SettingMMKV.darkThemeSetting == DarkThemeSetting.DarkMode) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            Color.Blue
                        }
                    }
                    systemDarkMode -> MaterialTheme.colorScheme.primary
                    else -> Color.Blue
                },
                style = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface)
            )
        }

        LinearButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 8.dp, bottom = 8.dp),
            onClick = onEditClick,
            text = stringResource(id = R.string.edit)
        )
    }
}