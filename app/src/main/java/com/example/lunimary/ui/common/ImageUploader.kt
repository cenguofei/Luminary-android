package com.example.lunimary.ui.common

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunimary.R
import com.example.lunimary.design.LoadingDialog
import com.example.lunimary.design.LocalSnackbarHostState
import com.example.lunimary.models.UploadData
import com.example.lunimary.models.fileBaseUrl
import com.example.lunimary.network.NetworkResult
import com.example.lunimary.network.asError
import com.example.lunimary.ui.edit.insertRow
import github.leavesczy.matisse.CoilImageEngine
import github.leavesczy.matisse.Matisse
import github.leavesczy.matisse.MediaResource
import github.leavesczy.matisse.MediaStoreCaptureStrategy
import github.leavesczy.matisse.MediaType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ImageUploader(
    mediaPickerLauncher: ManagedActivityResultLauncher<Matisse, List<MediaResource>?>,
    fileViewModel: FileViewModel,
    uploadState: MutableState<NetworkResult<UploadData>>,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {


}