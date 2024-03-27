package com.example.lunimary.ui.common

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import com.example.lunimary.models.UploadData
import com.example.lunimary.network.NetworkResult
import github.leavesczy.matisse.Matisse
import github.leavesczy.matisse.MediaResource
import kotlinx.coroutines.CoroutineScope

@Composable
fun ImageUploader(
    mediaPickerLauncher: ManagedActivityResultLauncher<Matisse, List<MediaResource>?>,
    fileViewModel: FileViewModel,
    uploadState: MutableState<NetworkResult<UploadData>>,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {


}