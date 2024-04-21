package com.example.lunimary.ui.topicselect

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.lunimary.R
import com.example.lunimary.model.Topic
import com.example.lunimary.ui.common.LabelSelectContainer

@Composable
fun RecommendTopic(viewModel: TopicSelectViewModel, recommendTopics: List<Topic>) {
    LabelSelectContainer(
        title = stringResource(id = R.string.recommend_topics)
    ) {
        LabelsContainer(
            topics = recommendTopics,
            showDelete = false,
            onClickTag = { viewModel.addRecommendTopic(it.id) }
        )
    }
}