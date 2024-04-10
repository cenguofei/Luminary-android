package com.example.lunimary.ui.edit.edit

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.EditText
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunimary.R
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.base.network.asError
import com.example.lunimary.design.LightAndDarkPreview
import com.example.lunimary.design.LoadingDialog
import com.example.lunimary.design.LunimaryGradientBackground
import com.example.lunimary.design.theme.LunimaryTheme
import com.example.lunimary.models.fileBaseUrl
import com.example.lunimary.ui.common.FileViewModel
import com.example.lunimary.ui.edit.EditViewModel
import com.example.lunimary.util.logd
import github.leavesczy.matisse.CoilImageEngine
import github.leavesczy.matisse.Matisse
import github.leavesczy.matisse.MatisseContract
import github.leavesczy.matisse.MediaResource
import github.leavesczy.matisse.MediaStoreCaptureStrategy
import github.leavesczy.matisse.MediaType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@SuppressLint("InflateParams")
@Composable
fun EditPage(
    viewModel: EditViewModel,
    onPreviewClick: () -> Unit,
    coroutineScope: CoroutineScope,
    onNavToWeb: () -> Unit,
    onShowMessage: (String) -> Unit,
    onShowSnackbar: (msg: String, label: String?) -> Unit
) {
    val context = LocalContext.current
    val bodyView = remember { LayoutInflater.from(context).inflate(R.layout.body_edit_text, null) }
    val bodyEditText = remember { bodyView.findViewById<EditText>(R.id.body_markdown_edit) }
    val titleView = remember { LayoutInflater.from(context).inflate(R.layout.title_edit_text, null) }
    val titleEditView = remember { titleView.findViewById<EditText>(R.id.title_edit) }

    val fileViewModel: FileViewModel = viewModel()
    val mediaPickerLauncher = rememberLauncherForActivityResult(
        contract = MatisseContract(),
        onResult = { result: List<MediaResource>? ->
            if (!result.isNullOrEmpty()) {
                val mediaResource = result[0]
                val uri = mediaResource.uri
                val path = mediaResource.path
                val name = mediaResource.name
                val mimeType = mediaResource.mimeType
                fileViewModel.updateSelectedFile(path, name)
                "mediaResource:uri=$uri, path=$path, name=$name, mimeType=$mimeType".logd("select_pic")
            }
            fileViewModel.updateShowImageSelector(false)
        }
    )
    when(fileViewModel.uploadState.value) {
        is NetworkResult.Loading -> {
            LoadingDialog(description = stringResource(id = R.string.uploading))
        }
        is NetworkResult.Success -> {
            LaunchedEffect(
                key1 = fileViewModel.uploadState.value,
                block = {
                    val url = (fileViewModel.uploadState.value as NetworkResult.Success).data?.filenames?.firstOrNull()
                    if (url != null) {
                        bodyEditText.insertRow("![Lunimary-Image](${fileBaseUrl + url})")
                    }
                    fileViewModel.clear()
                }
            )
        }
        is NetworkResult.Error -> {
            coroutineScope.launch {
                onShowSnackbar(
                    fileViewModel.uploadState.value.asError()?.msg.toString(),
                    null
                )
            }
        }
        else -> { }
    }
    LaunchedEffect(
        key1 = fileViewModel.showImageSelector.value,
        block = {
            if (fileViewModel.showImageSelector.value) {
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

    LaunchedEffect(
        key1 = Unit,
        block = {
            if (viewModel.uiState.value.isFillByArticle) {
                titleEditView.setText(viewModel.uiState.value.title)
                bodyEditText.setText(viewModel.uiState.value.body)
            }
        }
    )

    Column {
        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                AndroidTitleView(
                    titleView = titleView,
                    titleEditView = titleEditView,
                    viewModel = viewModel
                )
            }
            item {
                AndroidBodyView(
                    modifier = Modifier.weight(1f),
                    bodyView = bodyView,
                    bodyEditText = bodyEditText,
                    viewModel = viewModel
                )
            }
        }
        val showDialog = remember { mutableStateOf(false) }
        EditBottomOptions(
            onNavToWeb = onNavToWeb,
            bodyEditText = bodyEditText,
            fileViewModel = fileViewModel,
            onPreviewClick = onPreviewClick,
            showDialog = showDialog,
            onShowMessage = onShowMessage
        )
        AddLinkDialog(
            showDialog = showDialog,
            onFinish = { name, link ->
                bodyEditText.insertRow("[$name]($link)")
            }
        )
    }
}

@LightAndDarkPreview
@Composable
fun EditPagePreview() {
    LunimaryTheme {
        LunimaryGradientBackground {
            EditPage(
                viewModel = viewModel(),
                onPreviewClick = {},
                coroutineScope = rememberCoroutineScope(),
                onNavToWeb = {},
                onShowMessage = {},
                onShowSnackbar = {_,_ ->}
            )
        }
    }
}