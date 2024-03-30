package com.example.lunimary.ui.user.information

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.lunimary.R
import com.example.lunimary.design.LoadingDialog
import com.example.lunimary.design.LocalSnackbarHostState
import com.example.lunimary.models.User
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.base.network.asError
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.Screens
import com.example.lunimary.base.UserState
import com.example.lunimary.base.currentUser
import com.example.lunimary.util.logd
import github.leavesczy.matisse.CoilImageEngine
import github.leavesczy.matisse.Matisse
import github.leavesczy.matisse.MatisseContract
import github.leavesczy.matisse.MediaResource
import github.leavesczy.matisse.MediaStoreCaptureStrategy
import github.leavesczy.matisse.MediaType
import kotlinx.coroutines.launch

fun NavGraphBuilder.informationScreen(
    appState: LunimaryAppState
) {
    composable(
        route = Screens.Information.route
    ) {
        val informationViewModel: InformationViewModel = viewModel()
        val user = UserState.currentUserState.observeAsState()
        InformationScreen(
            onBack = appState::popBackStack,
            informationViewModel = informationViewModel,
            user = user.value!!
        )
    }
}

val coverHeight = 200.dp

@Composable
fun InformationScreen(
    user: User,
    informationViewModel: InformationViewModel,
    onBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollableState = rememberScrollableState(consumeScrollDelta = { it })

    val mediaPickerLauncher = rememberLauncherForActivityResult(
        contract = MatisseContract(),
        onResult = { result: List<MediaResource>? ->
            if (!result.isNullOrEmpty()) {
                val mediaResource = result[0]
                val uri = mediaResource.uri
                val path = mediaResource.path
                val name = mediaResource.name
                val mimeType = mediaResource.mimeType
                informationViewModel.uploadImage(path, name)
                "mediaResource:uri=$uri, path=$path, name=$name, mimeType=$mimeType".logd("select_pic")
            }
            informationViewModel.updateShowImageSelector(false, UploadUserFileType.None)
        }
    )
    when (informationViewModel.headUploadState.value) {
        is NetworkResult.Loading -> {
            LoadingDialog(description = stringResource(id = R.string.uploading))
        }

        is NetworkResult.Success -> {
            LaunchedEffect(
                key1 = informationViewModel.headUploadState.value,
                block = {
                    val url = (informationViewModel.headUploadState.value as NetworkResult.Success)
                        .data?.filenames?.firstOrNull()
                    url?.let { UserState.updateLocalUser(user = currentUser.copy(headUrl = it)) }
                    informationViewModel.clear()
                }
            )
        }

        is NetworkResult.Error -> {
            val snackbarHostState = LocalSnackbarHostState.current.snackbarHostState
            coroutineScope.launch {
                snackbarHostState?.showSnackbar(message = informationViewModel.headUploadState.value.asError()?.msg.toString())
            }
        }

        else -> {}
    }
    when (informationViewModel.backgroundUploadState.value) {
        is NetworkResult.Loading -> {
            LoadingDialog(description = stringResource(id = R.string.uploading))
        }

        is NetworkResult.Success -> {
            LaunchedEffect(
                key1 = informationViewModel.backgroundUploadState.value,
                block = {
                    val url = (informationViewModel.backgroundUploadState.value as NetworkResult.Success)
                        .data?.filenames?.firstOrNull()
                    url?.let { UserState.updateLocalUser(user = currentUser.copy(background = it)) }
                    informationViewModel.clear()
                }
            )
        }

        is NetworkResult.Error -> {
            val snackbarHostState = LocalSnackbarHostState.current.snackbarHostState
            coroutineScope.launch {
                snackbarHostState?.showSnackbar(message = informationViewModel.headUploadState.value.asError()?.msg.toString())
            }
        }

        else -> {}
    }
    LaunchedEffect(
        key1 = informationViewModel.showImageSelector.value,
        block = {
            if (informationViewModel.showImageSelector.value) {
                val matisse = Matisse(
                    maxSelectable = 1,
                    imageEngine = CoilImageEngine(),
                    mediaType = MediaType.ImageOnly,
                    singleMediaType = true,
                    captureStrategy = MediaStoreCaptureStrategy()
                )
                mediaPickerLauncher.launch(matisse)
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scrollable(state = scrollableState, orientation = Orientation.Vertical),
        contentAlignment = Alignment.TopStart
    ) {
        Cover(
            height = coverHeight,
            coverUrl = user.realBackgroundUrl(),
        )
        val headImageSize = 100.dp
        Column(
            modifier = Modifier
                .padding(top = coverHeight)
                .fillMaxWidth()
        ) {
            InformationItems(
                user = user,
                headImageSize = headImageSize
            )
        }

        EditableHeadImage(
            modifier = Modifier,
            headUrl = user.realHeadUrl(),
            onClick = {
                informationViewModel.updateShowImageSelector(true, UploadUserFileType.HeadImage)
            },
            size = headImageSize
        )

        ChangeCover(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding(),
            onClick = {
                informationViewModel.updateShowImageSelector(true, UploadUserFileType.Background)
            }
        )

        IconButton(
            onClick = onBack,
            modifier = Modifier.statusBarsPadding()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )
        }
    }
}

