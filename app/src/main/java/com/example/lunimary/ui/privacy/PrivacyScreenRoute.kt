package com.example.lunimary.ui.privacy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.base.network.asError
import com.example.lunimary.base.network.asSuccess
import com.example.lunimary.design.LunimaryMarkdown
import com.example.lunimary.design.LunimaryToolbar
import com.example.lunimary.design.nicepage.LunimaryStateContent
import com.example.lunimary.util.empty

@Composable
fun PrivacyScreenRoute(onBack: () -> Unit, viewModel: PrivacyViewModel) {
    val data = viewModel.privacy.value
    Column(modifier = Modifier.fillMaxSize()) {
        LunimaryToolbar(
            onBack = onBack,
            modifier = Modifier.statusBarsPadding()
        )
        when(data) {
            is NetworkResult.Loading -> {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            else -> { }
        }
        LunimaryStateContent(
            error = data is NetworkResult.Error,
            errorMsg = data.asError()?.msg
        ) {
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)) {
                item { LunimaryMarkdown(markdown = data.asSuccess()?.data ?: empty) }
            }
        }
    }
}