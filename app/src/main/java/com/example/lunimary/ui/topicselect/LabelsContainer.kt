package com.example.lunimary.ui.topicselect

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lunimary.model.Topic
import com.example.lunimary.ui.common.TagColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LabelsContainer(
    topics: List<Topic>,
    onClickTag: (Topic) -> Unit = {},
    onDelete: (Topic) -> Unit = {},
    showDelete: Boolean
) {
    if (topics.isEmpty()) return
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        topics.forEach {
            val tagColor = TagColors.getColor(it.topic)
            Box(contentAlignment = Alignment.Center) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = tagColor,
                    contentColor = Color.White,
                    onClick = { onClickTag(it) }
                ) {
                    Text(
                        text = it.topic,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                if (showDelete) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.TopEnd),
                        shape = RoundedCornerShape(50),
                        onClick = { onDelete(it) },
                    ) {
                        Box(Modifier.fillMaxSize()) {
                            Spacer(
                                modifier = Modifier
                                    .width(8.dp)
                                    .height(1.dp)
                                    .background(Color.White)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}