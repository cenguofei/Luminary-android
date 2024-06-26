package com.example.lunimary.ui.edit.bottomsheet

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.lunimary.R
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.base.network.asError
import com.example.lunimary.design.LoadingDialog
import com.example.lunimary.model.fileBaseUrl
import com.example.lunimary.ui.common.LabelSelectContainer
import com.example.lunimary.ui.edit.EditViewModel
import com.example.lunimary.util.logd
import github.leavesczy.matisse.CoilImageEngine
import github.leavesczy.matisse.Matisse
import github.leavesczy.matisse.MatisseContract
import github.leavesczy.matisse.MediaResource
import github.leavesczy.matisse.MediaStoreCaptureStrategy
import github.leavesczy.matisse.MediaType

@Composable
fun ArticleCover(
    editViewModel: EditViewModel,
    onShowSnackbar: (msg: String, label: String?) -> Unit
) {
    val showImageSelector = remember { mutableStateOf(false) }
    val uploadSuccess = remember { mutableStateOf(false) }
    val mediaPickerLauncher = rememberLauncherForActivityResult(
        contract = MatisseContract(),
        onResult = { result: List<MediaResource>? ->
            if (!result.isNullOrEmpty()) {
                val mediaResource = result[0]
                val uri = mediaResource.uri
                val path = mediaResource.path
                val name = mediaResource.name
                val mimeType = mediaResource.mimeType
                editViewModel.updateUri(uri)
                editViewModel.uploadFile(path = path, filename = name)
                "mediaResource:uri=$uri, path=$path, name=$name, mimeType=$mimeType".logd("select_pic")
                uploadSuccess.value = false
            }
            showImageSelector.value = false
        }
    )
    when(editViewModel.uploadCoverState.value) {
        is NetworkResult.Loading -> {
            LoadingDialog(description = stringResource(id = R.string.uploading))
        }
        is NetworkResult.Success -> {
            uploadSuccess.value = true
        }
        is NetworkResult.Error -> {
            LaunchedEffect(
                key1 = Unit,
                block = {
                    onShowSnackbar(
                        editViewModel.uploadCoverState.value.asError()?.msg.toString(),
                        null
                    )
                }
            )
        }
        else -> { }
    }
    LaunchedEffect(
        key1 = showImageSelector.value,
        block = {
            if (showImageSelector.value) {
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
    LabelSelectContainer(title = stringResource(id = R.string.article_cover)) {
        Surface(
            modifier = Modifier
                .height(100.dp)
                .width(180.dp),
            onClick = { showImageSelector.value = true },
            shape = RoundedCornerShape(16),
            color = Color.Gray.copy(alpha = 0.1f)
        ) {
            val uiState = editViewModel.uiState
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (uiState.cover.isNotBlank()) {
                    AsyncImage(
                        model = fileBaseUrl + uiState.cover,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else if (uiState.uri != Uri.EMPTY && uploadSuccess.value) {
                    AsyncImage(
                        model = uiState.uri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                        Text(
                            text = stringResource(id = R.string.publish_cover),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.article_cover_suggestion),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            style = MaterialTheme.typography.bodySmall
        )
    }
}
