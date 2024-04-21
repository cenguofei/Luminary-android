package com.example.lunimary.ui.topicselect

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.Screens

fun NavGraphBuilder.topicSelectScreen(
    appState: LunimaryAppState,
    onShowSnackbar: (msg: String, label: String?) -> Unit
) {
    composable(
        route = Screens.TopicSelect.route
    ) {
        TopicSelectRoute(
            onBack = appState::popBackStack,
            coroutineScope = appState.coroutineScope
        )
    }
}