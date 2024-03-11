package com.example.lunimary.ui.edit

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.EditText
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunimary.R
import com.example.lunimary.design.ChineseMarkdownWeb
import com.example.lunimary.design.LinearButton
import com.example.lunimary.design.LunimaryGradientBackground
import com.example.lunimary.design.LightAndDarkPreview
import com.example.lunimary.design.LunimaryWebView
import com.example.lunimary.design.theme.LunimaryTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay


@SuppressLint("InflateParams")
@Composable
fun EditPage(
    viewModel: EditViewModel,
    onPreviewClick: () -> Unit,
    coroutineScope: CoroutineScope,
    onNavToWeb: () -> Unit
) {
    val context = LocalContext.current
    val bodyView = remember { LayoutInflater.from(context).inflate(R.layout.body_edit_text, null) }
    val bodyEditText = remember { bodyView.findViewById<EditText>(R.id.body_markdown_edit) }
    val titleView = remember { LayoutInflater.from(context).inflate(R.layout.title_edit_text, null) }
    val titleEditView = remember { titleView.findViewById<EditText>(R.id.title_edit) }
    LaunchedEffect(
        key1 = viewModel.articleDataState.value,
        block = {
            if (viewModel.isFillByArticle) {
                titleEditView.setText(viewModel.articleDataState.value.title)
                bodyEditText.setText(viewModel.articleDataState.value.body)
            }
        }
    )

    Column {
        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                val titleColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f).toArgb()
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    factory = {
                        titleEditView.apply {
                            setTextColor(titleColor)
                            setHintTextColor(titleColor)
                            requestFocus()
                            addTextChangedListener(
                                afterTextChanged = {
                                    viewModel.title = text.toString()
                                }
                            )
                        }
                        titleView
                    }
                )
            }
            item {
                val bodyColor = MaterialTheme.colorScheme.onSurface.toArgb()
                AndroidView(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    factory = {
                        bodyEditText.apply {
                            setTextColor(bodyColor)
                            setHintTextColor(bodyColor)
                            addTextChangedListener(
                                afterTextChanged = {
                                    viewModel.body = text.toString()
                                }
                            )
                        }
                        bodyView
                    }
                )
            }
        }

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
                LazyRow(modifier = Modifier.padding(end = 100.dp)) {
                    items(options.size) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = editOptions[it], contentDescription = null)
                        }
                    }
                }

                LinearButton(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp),
                    onClick = {
                        keyboardController?.hide()
                        onPreviewClick()
                    },
                    text = stringResource(id = R.string.preview)
                )
            }
        }
    }
}

@Composable
private fun ShowStudyMarkdown(
    onNavToWeb: () -> Unit
) {
    var shouldShowLearnMDTips by rememberSaveable { mutableStateOf(true) }
    LaunchedEffect(key1 = shouldShowLearnMDTips) {
        if (shouldShowLearnMDTips) {
            coroutineScope {
                delay(5000)
                shouldShowLearnMDTips = false
            }
        }
    }
    if (shouldShowLearnMDTips) {
        Snackbar(
            modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 8.dp),
            action = {
                TextButton(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    onClick = {
                        shouldShowLearnMDTips = false
                        onNavToWeb()
                    }
                ) {
                    Text(text = stringResource(id = R.string.ok))
                }
            },
            dismissAction = {
                TextButton(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    onClick = { shouldShowLearnMDTips = false }
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
            content = {
                Text(text = stringResource(id = R.string.study_markdown))
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
                onNavToWeb = {}
            )
        }
    }
}