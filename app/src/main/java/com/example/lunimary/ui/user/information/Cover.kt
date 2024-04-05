package com.example.lunimary.ui.user.information

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import com.example.lunimary.R

@Composable
fun Cover(
    height: Dp,
    coverUrl: String
) {
    Box(modifier = Modifier
        .height(height)
        .fillMaxWidth()) {
        AsyncImage(
            model = coverUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.cover),
            error = painterResource(id = R.drawable.cover),
            fallback = painterResource(id = R.drawable.cover)
        )
    }
}
