package com.example.lunimary.ui.edit.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lunimary.R
import com.example.lunimary.design.LunimaryMarkdown
import com.example.lunimary.design.components.LinearButton
import com.example.lunimary.ui.edit.EditViewModel

@Composable
fun PreviewPage(viewModel: EditViewModel, onEditClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)) {
            Text(
                text = viewModel.uiState.title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            LunimaryMarkdown(markdown = viewModel.uiState.body.trimIndent())
        }

        LinearButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp),
            onClick = onEditClick,
            text = stringResource(id = R.string.edit)
        )
    }
}