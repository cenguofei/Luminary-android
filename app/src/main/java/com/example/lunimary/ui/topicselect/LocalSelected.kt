package com.example.lunimary.ui.topicselect

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.lunimary.R
import com.example.lunimary.model.Topic
import com.example.lunimary.ui.common.LabelSelectContainer

@Composable
fun LocalSelected(
    viewModel: TopicSelectViewModel,
    userSelectedTopics: List<Topic>,
) {
    var showDelete by remember { mutableStateOf(false) }
    LabelSelectContainer(
        title = stringResource(id = R.string.selected_topics),
        titleRight = {
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                onClick = { showDelete = !showDelete },
            ) {
                val textId = if (showDelete) R.string.finish else R.string.edit_cn
                Text(text = stringResource(id = textId))
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
    ) {
        LabelsContainer(
            topics = userSelectedTopics,
            onDelete = {
                viewModel.deleteSelectedTopic(it.id)
            },
            showDelete = showDelete
        )
    }
}