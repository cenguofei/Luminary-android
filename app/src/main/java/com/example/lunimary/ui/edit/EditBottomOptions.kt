package com.example.lunimary.ui.edit

import android.widget.EditText
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.lunimary.R
import com.example.lunimary.design.LinearButton
import com.example.lunimary.ui.common.FileViewModel

@Composable
fun EditBottomOptions(
    onNavToWeb: () -> Unit,
    bodyEditText: EditText,
    fileViewModel: FileViewModel,
    onPreviewClick: () -> Unit,
    showDialog: MutableState<Boolean>
) {
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxWidth()
        .imePadding()) {
        ShowStudyMarkdown(onNavToWeb = onNavToWeb)
        val images = formatImages
        val keyboardController = LocalSoftwareKeyboardController.current
        LazyRow {
            items(images.size) { index ->
                IconButton(
                    onClick = { bodyEditText.handleFormat(index, context) }
                ) {
                    Icon(imageVector = formatImages[index], contentDescription = null)
                }
            }
        }
        Box(contentAlignment = Alignment.Center) {
            val options = editOptions
            LazyRow(modifier = Modifier
                .fillMaxWidth()
                .padding(end = 100.dp)) {
                itemsIndexed(options) { index, item ->
                    IconButton(
                        onClick = {
                            when(index) {
                                0 -> { fileViewModel.updateShowImageSelector(true) }
                                1 -> { showDialog.value = true }
                            }
                        }
                    ) { Icon(imageVector = item, contentDescription = null) }
                }
            }

            LinearButton(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp, bottom = 16.dp),
                onClick = {
                    keyboardController?.hide()
                    onPreviewClick()
                },
                text = stringResource(id = R.string.preview)
            )
        }
    }
}