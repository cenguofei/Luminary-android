package com.example.lunimary.ui.edit

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Undo
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.lunimary.R
import com.example.lunimary.util.log
import github.leavesczy.matisse.DefaultMediaFilter
import github.leavesczy.matisse.Matisse
import github.leavesczy.matisse.MatisseContract
import github.leavesczy.matisse.MediaResource
import github.leavesczy.matisse.MimeType

val editOptions: List<ImageVector>
    @Composable get() = listOf(
        Icons.Default.Image,
        ImageVector.vectorResource(id = R.drawable.divider),
        Icons.Default.AddLink, Icons.Default.Undo,
        Icons.Default.Redo, Icons.Default.Settings
    )


@Composable
fun SelectPictures() {
    val mediaPickerLauncher =
        rememberLauncherForActivityResult(contract = MatisseContract()) { result: List<MediaResource>? ->
            if (!result.isNullOrEmpty()) {
                val mediaResource = result[0]
                val uri = mediaResource.uri
                val path = mediaResource.path
                val name = mediaResource.name
                val mimeType = mediaResource.mimeType
                "selected image{uri=$uri, path=$path, name=$name, mimeType=$mimeType}".log("select_picture")
            }
        }

    val matisse = Matisse(
        maxSelectable = 1,
        mediaFilter = DefaultMediaFilter(supportedMimeTypes = MimeType.ofImage()),
        imageEngine = CoilImageEngine(),
        singleMediaType = true,
        captureStrategy = null
    )
    mediaPickerLauncher.launch(matisse)
}