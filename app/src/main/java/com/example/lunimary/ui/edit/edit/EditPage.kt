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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunimary.R
import com.example.lunimary.base.network.NetworkResult
import com.example.lunimary.base.network.asError
import com.example.lunimary.design.LoadingDialog
import com.example.lunimary.model.fileBaseUrl
import com.example.lunimary.ui.common.FileViewModel
import com.example.lunimary.ui.edit.EditViewModel
import com.example.lunimary.util.logd
import github.leavesczy.matisse.CoilImageEngine
import github.leavesczy.matisse.Matisse
import github.leavesczy.matisse.MatisseContract
import github.leavesczy.matisse.MediaResource
import github.leavesczy.matisse.MediaStoreCaptureStrategy
import github.leavesczy.matisse.MediaType


@SuppressLint("InflateParams")
@Composable
fun EditPage(
    viewModel: EditViewModel,
    onPreviewClick: () -> Unit,
    onNavToWeb: () -> Unit,
    onShowSnackbar: (msg: String, label: String?) -> Unit,
    onPublishedArticleDelete: () -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val bodyView = remember { LayoutInflater.from(context).inflate(R.layout.body_edit_text, null) }
    val bodyEditText = remember { bodyView.findViewById<EditText>(R.id.body_markdown_edit) }
    val titleView =
        remember { LayoutInflater.from(context).inflate(R.layout.title_edit_text, null) }
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
    when (fileViewModel.uploadState.value) {
        is NetworkResult.Loading -> {
            LoadingDialog(description = stringResource(id = R.string.uploading))
        }

        is NetworkResult.Success -> {
            LaunchedEffect(
                key1 = fileViewModel.uploadState.value,
                block = {
                    val url =
                        (fileViewModel.uploadState.value as NetworkResult.Success).data?.filenames?.firstOrNull()
                    if (url != null) {
                        bodyEditText.insertRow("![Lunimary-Image](${fileBaseUrl + url})")
                    }
                    fileViewModel.clear()
                }
            )
        }

        is NetworkResult.Error -> {
            onShowSnackbar(
                fileViewModel.uploadState.value.asError()?.msg.toString(),
                null
            )
        }

        else -> {}
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
            if (viewModel.uiState.isFillByArticle) {
                titleEditView.setText(viewModel.uiState.title)
                bodyEditText.setText(viewModel.uiState.body)
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
        val showAddLinkDialog = remember { mutableStateOf(false) }
        EditBottomOptions(
            onNavToWeb = onNavToWeb,
            bodyEditText = bodyEditText,
            fileViewModel = fileViewModel,
            onPreviewClick = onPreviewClick,
            showAddLinkDialog = showAddLinkDialog,
            viewModel = viewModel,
            onPublishedArticleDelete = onPublishedArticleDelete,
            onShowSnackbar = onShowSnackbar,
            onBack = onBack
        )
        AddLinkDialog(
            showDialog = showAddLinkDialog,
            onFinish = { name, link ->
                bodyEditText.insertRow("[$name]($link)")
            }
        )
    }
}