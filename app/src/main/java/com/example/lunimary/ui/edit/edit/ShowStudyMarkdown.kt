package com.example.lunimary.ui.edit.edit

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.lunimary.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

@Composable
fun ShowStudyMarkdown(
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