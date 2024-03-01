package com.example.lunimary.ui.message

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MessageScreen(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(text = "Message", modifier = Modifier.align(Alignment.Center))
    }
}