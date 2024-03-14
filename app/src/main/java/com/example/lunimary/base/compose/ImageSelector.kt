package com.example.lunimary.base.compose

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import coil.Coil
import com.example.lunimary.util.logd
import github.leavesczy.matisse.CaptureStrategy
import github.leavesczy.matisse.CoilImageEngine
import github.leavesczy.matisse.Matisse
import github.leavesczy.matisse.MatisseContract
import github.leavesczy.matisse.MediaResource
import github.leavesczy.matisse.MediaStoreCaptureStrategy
import github.leavesczy.matisse.MediaType
import github.leavesczy.matisse.SmartCaptureStrategy

@Composable
fun ImageSelector(
    showSelector: Boolean = false,
    onResult: (MediaResource) -> Unit
) {
    if (showSelector) {

    }
}