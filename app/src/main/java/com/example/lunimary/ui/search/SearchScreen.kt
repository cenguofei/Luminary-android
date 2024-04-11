package com.example.lunimary.ui.search

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.lunimary.ui.LunimaryAppState
import com.example.lunimary.ui.Screens

fun NavGraphBuilder.searchScreen(
    appState: LunimaryAppState
) {
    composable(Screens.Search.route) {
        SearchScreenRoute(
            appState = appState,
            onBack = appState::popBackStack,
        )
    }
}